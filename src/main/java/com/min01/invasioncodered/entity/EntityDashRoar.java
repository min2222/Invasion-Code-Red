package com.min01.invasioncodered.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class EntityDashRoar extends Entity
{
	boolean isBack;
	LivingEntity owner;
	public EntityDashRoar(EntityType<?> p_19870_, Level p_19871_) 
	{
		super(p_19870_, p_19871_);
	}
	
	public EntityDashRoar(EntityType<?> p_19870_, Level p_19871_, LivingEntity owner, boolean isBack) 
	{
		super(p_19870_, p_19871_);
		this.owner = owner;
		this.isBack = isBack;
	}
	
	@Override
	public void tick() 
	{
		super.tick();
		if(this.tickCount >= 2 && this.owner != null)
		{
			if(this.isBack)
			{
				this.strongKnockback(this.owner, 0, 4.5);
			}
			else
			{
				this.strongKnockback(this.owner, 0, 7);
			}
			this.discard();
		}
	}
	
    private void strongKnockback(Entity p_33340_, double vertical, double horizontal) 
    {
    	double d0 = p_33340_.getX() - this.getX();
        double d1 = p_33340_.getZ() - this.getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        p_33340_.push(d0 / d2 * horizontal, vertical, d1 / d2 * horizontal);
    }
	
	@Override
	public boolean displayFireAnimation()
	{
		return false;
	}

	@Override
	protected void defineSynchedData() 
	{
		
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag p_20052_) 
	{
		
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag p_20139_)
	{
		
	}

	@Override
	public Packet<?> getAddEntityPacket() 
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
