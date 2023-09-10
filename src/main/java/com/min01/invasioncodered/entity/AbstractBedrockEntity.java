package com.min01.invasioncodered.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public abstract class AbstractBedrockEntity extends Monster
{
	private static final EntityDataAccessor<Integer> SKIN_ID = SynchedEntityData.defineId(AbstractBedrockEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<String> STATE = SynchedEntityData.defineId(AbstractBedrockEntity.class, EntityDataSerializers.STRING);
	private static final EntityDataAccessor<Boolean> IS_DELAYED_ATTACKING = SynchedEntityData.defineId(AbstractBedrockEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> IS_EVENT_FIRED = SynchedEntityData.defineId(AbstractBedrockEntity.class, EntityDataSerializers.BOOLEAN);
	
	public AbstractBedrockEntity(EntityType<? extends Monster> p_33002_, Level p_33003_) 
	{
		super(p_33002_, p_33003_);
	}
    
    @Override
    protected void defineSynchedData() 
    {
    	super.defineSynchedData();
    	this.entityData.define(SKIN_ID, 0);
    	this.entityData.define(STATE, "");
    	this.entityData.define(IS_DELAYED_ATTACKING, false);
    	this.entityData.define(IS_EVENT_FIRED, false);
    }
    
    public void setEventFired(boolean value)
    {
    	this.entityData.set(IS_EVENT_FIRED, value);
    }
    
    public boolean isEventFired()
    {
    	return this.entityData.get(IS_EVENT_FIRED);
    }
    
    public void setDelayedAttacking(boolean value)
    {
    	this.entityData.set(IS_DELAYED_ATTACKING, value);
    }
    
    public boolean isDelayedAttacking()
    {
    	return this.entityData.get(IS_DELAYED_ATTACKING);
    }
    
    public void setState(String value)
    {
    	this.entityData.set(STATE, value);
    }
    
    public String getState()
    {
    	return this.entityData.get(STATE);
    }
    
    public void setSkinID(int value)
    {
    	this.entityData.set(SKIN_ID, value);
    }
    
    public int getSkinID()
    {
    	return this.entityData.get(SKIN_ID);
    }
    
    public double getMeleeAttackRangeSqr(LivingEntity p_147273_, float multiplier)
    {
    	return (double)(this.getBbWidth() * multiplier * this.getBbWidth() * multiplier + p_147273_.getBbWidth());
    }

    public boolean isWithinMeleeAttackRange(LivingEntity p_217067_, float multiplier)
    {
    	double d0 = this.distanceToSqr(p_217067_.getX(), p_217067_.getY(), p_217067_.getZ());
    	return d0 <= this.getMeleeAttackRangeSqr(p_217067_, multiplier);
    }
}
