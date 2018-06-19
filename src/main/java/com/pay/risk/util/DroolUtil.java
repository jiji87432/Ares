package com.pay.risk.util;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.StatefulSession;
import org.drools.compiler.PackageBuilder;

public class DroolUtil {
	public static StatefulSession getDroolsSession(String path){
		try{
			RuleBase  ruleBase = RuleBaseFactory.newRuleBase();
			PackageBuilder backageBuilder = getPackageBuilderFromDrlFile(path);
			ruleBase.addPackages(backageBuilder.getPackages());
			StatefulSession statefulSession = ruleBase.newStatefulSession();
			return statefulSession;
		}catch(Exception e ){
			e.printStackTrace();
			return null;
		}
		
	}
	
	private static PackageBuilder getPackageBuilderFromDrlFile(String path) throws Exception {
		List<String> drlFilePath = getTestDrlFile(path);
		// 装载测试脚本文件
		List<Reader> readers = readRuleFromDrlFile(drlFilePath);

		PackageBuilder backageBuilder = new PackageBuilder();
		for (Reader r : readers) {
			backageBuilder.addPackageFromDrl(r);
			r.close();
		}
		
		// 检查脚本是否有问题
		if(backageBuilder.hasErrors()) {
			throw new Exception(backageBuilder.getErrors().toString());
		}
		
		return backageBuilder;
	}

	private static List<Reader> readRuleFromDrlFile(List<String> drlFilePath) throws Exception {
		if (null == drlFilePath || 0 == drlFilePath.size()) {
			return null;
		}

		List<Reader> readers = new ArrayList<Reader>();

		for (String ruleFilePath : drlFilePath) {
			readers.add(new FileReader(new File(ruleFilePath)));
			//readers.add(new BufferedReader(new InputStreamReader(DroolUtil.class.getClassLoader().getResourceAsStream(ruleFilePath))));
		}
		
		return readers;
	}

	private static List<String> getTestDrlFile(String path) {
		List<String> drlFilePath = new ArrayList<String>();
		drlFilePath
				.add(path);

		return drlFilePath;
	}
	
}
