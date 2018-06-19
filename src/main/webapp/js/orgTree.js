$(function(){
			var treeData;
			//初始化tree 固定名称 
			$('#organizationTree').tree({
				animate:true,
				url: 'organizationQuery.action?pid=00',
				isLeaf:false,
				cascadeCheck:false,
				onLoadSuccess:function(data){
					
				},
				onClick:function(node){
					var curr = node;	
				},
				onBeforeExpand:function(node,param){
			         $('#organizationTree').tree('options').url = "organizationQuery.action?pid="+ node.id;                       
         	 	 }
			});

			//弹出对话框  固定名称
			var showTree = $("#showTree");
			var address = "";
			
			//选择组织机构点击事件  
			$("a[id=searchOrg]").click(function(){
				var o = $(this);
				var depth = 0;
				if($(this).attr("depth")){
					depth = $(this).attr("depth");
				}
				if($(this).attr("singleSelection")){
					//$('#organizationTree').tree('options').onlyLeafCheck = true;
				}
				if($(this).attr("onlyLeafCheck")){
					$('#organizationTree').tree('options').onlyLeafCheck = true;
				}
				if($(this).attr("cascadeCheck")){
					$('#organizationTree').tree('options').cascadeCheck = true;
				}
				if($(this).attr("checkbox")){
					$('#organizationTree').tree('options').checkbox = true;
				}else{
					//不是多选的情况下 可双击选择
					$('#organizationTree').tree('options').onDblClick = function(node){
						fillData(o,getAddress(node,depth,0),getLeafNode(node).id);
						showTree.dialog("close");
					}
				}
				//collapseAll(getSelected());
				//reload();
				showTree.dialog({
					title:'组织机构',
					resizable: false,
					buttons:{ 
						"确定": function() { 
							if(o.attr("checkbox")){
								fillData(o,getChecked(depth),leafNode.id.concat(leafNodeIds));
							}else{
								node = getSelected();
								fillData(o,getAddress(node,depth,0),getLeafNode(node).id);
							}
							//关闭
							$(this).dialog("close");
						 },
						"取消": 	function() { $(this).dialog("close"); }
					},
					width:200,
					minHeight:100,
					maxHeight:200,
					position:[o.offset().left,o.offset().top+30]
				});	
			});
			$("a[id=clearOrg]").click(function(){
				$(this).siblings("input[disabled=true]").val("");
				var treeName = $(this).siblings("a[id=searchOrg]").attr("name");
				treeName = treeName.substring(0,treeName.length-6);
				$(this).siblings("input[name="+treeName+"]").val("");
			});
			
		});
		var leafNode = null;
		var leafNodeIds = null;
		/**
		*获取当前check的节点
		*/
		function getChecked(depth){
			var nodes = $('#organizationTree').tree('getChecked');
			var currentNode = nodes[0];
			address = "";
			//保存最下级节点(不一定是叶子)
			leafNode = currentNode = getLeafNode(currentNode);
			//叶子节点地址
			leafAddress = ""; 
			leafNodeIds = "";
			if(nodes.length > 1){
				var firstIn = true;
				for ( var i = 0; i < nodes.length; i++) {
					if(isLeaf(nodes[i])){
						if(firstIn){firstIn=false;i++};									//从第二叶子开始 
						leafAddress = leafAddress.concat(","+nodes[i].text);			//拼接余下地址
						leafNodeIds = leafNodeIds.concat(","+nodes[i].id);				//拼接余下地区编码
					}
				}
			}
			
			return getAddress(currentNode,depth,0).concat(leafAddress);
		}

		function getLeafNode(currentNode){
			//选择的节点为父节点  默认获取自身下第一个子节点
			if(!isLeaf(currentNode)){
				//如果不是叶子节点 获取下一级 (无论下一级是否叶子节点)
				firstChildren = getChildren(currentNode)[0];
				if(firstChildren)currentNode = firstChildren;
			}
			return currentNode;
		}

		/**
		*获取所选地址字符串形式
		*currentNode:当前节点
		*depth:遍历深度(地址显示到哪一级别)
		*currentDepth:当前遍历深度 
		**/
		function getAddress(currentNode,depth,currentDepth){
			address="";
			parentNode = getParent(currentNode);
			while(null != currentNode){
				if((depth!=0 && currentDepth < (depth-1)) || depth == 0){
					if(null != parentNode){
						address = getAddress(parentNode,depth,currentDepth+1).concat(currentNode.text);
						break;
					}
				}
				address = address.concat(currentNode.text);
				break;
			}
			return address;
		}
		//当前节点是否叶子节点
		function isLeaf(node){
			return $("#organizationTree").tree('isLeaf',node.target);
		}
		//获取当前节点的父节点
		function getParent(node){
			return $('#organizationTree').tree('getParent', node.target);
		}
		//获取当前节点下所有子节点
		function getChildren(node){    
			return $("#organizationTree").tree('getChildren',node.target);
		}
		//折叠全部 
		function collapseAll(node){
			if(node)$('#organizationTree').tree('collapseAll',node.target);
			else $('#organizationTree').tree('collapseAll');
		}
		//获取当前selected
		function getSelected(){
			return $('#organizationTree').tree('getSelected');
		}
		//reload
		function reload(){
			$('#organizationTree').tree('reload');
		}
		/**
		*获取当前节点根 
		*/
		function getRoot(node){
			return null != getParent(node)?getRoot(getParent(node)):node;
		}
		function removes(node){
			$('#organizationTree').tree('uncheck',node.target);
		}
		//填充数据
		function fillData(o,data,leafNodeId){
			var treeName = o.attr("name");
			treeName = treeName.substring(0,treeName.length-6);
			o.siblings("input[name="+treeName+"Show]:disabled").val(data);
			o.siblings("input[name="+treeName+"]").val(leafNodeId);
		}