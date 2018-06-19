/**
 * 
 */
package com.pay.risk.entity;

/**
 * @Description: id 、optimistic
 * @see: AutoIDEntity 此处填写需要参考的类
 * @version 2014年8月14日 上午11:02:33
 * @author jiude.sun
 */
public class AutoIDEntity
{
    /** id */
    private Long id;
    
    /** 版本 */
    private Integer optimistic;
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public Integer getOptimistic()
    {
        return optimistic;
    }
    
    public void setOptimistic(Integer optimistic)
    {
        this.optimistic = optimistic;
    }
    
}
