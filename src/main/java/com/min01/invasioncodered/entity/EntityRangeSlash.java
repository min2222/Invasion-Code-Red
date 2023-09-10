package com.min01.invasioncodered.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class EntityRangeSlash extends ThrowableProjectile implements IAnimatable
{
	private static final EntityDataAccessor<Boolean> IS_BIG = SynchedEntityData.defineId(EntityRangeSlash.class, EntityDataSerializers.BOOLEAN);
	
	public EntityRangeSlash(EntityType<? extends ThrowableProjectile> p_36721_, Level p_36722_)
	{
		super(p_36721_, p_36722_);
		this.setNoGravity(true);
	}
	
	@Override
	public boolean displayFireAnimation() 
	{
		return false;
	}
	
	@Override
	protected void onHitBlock(BlockHitResult p_36755_)
	{
		super.onHitBlock(p_36755_);
		this.discard();
	}
	
	@Override
	public void tick()
	{
		super.tick();
		if(this.tickCount >= 200)
		{
			this.discard();
		}
	}
	
	@Override
	protected void onHitEntity(EntityHitResult p_36757_) 
	{
		super.onHitEntity(p_36757_);
		Entity entity = p_36757_.getEntity();
		if(entity != null)
		{
			if(entity != this.getOwner())
			{
				p_36757_.getEntity().hurt(DamageSource.mobAttack((LivingEntity) this.getOwner()), this.isBig() ? 26 : 12);
			}
		}
		else
		{
			this.discard();
		}
	}

	@Override
	public void registerControllers(AnimationData data) 
	{
		
	}

	@Override
	public AnimationFactory getFactory() 
	{
		return GeckoLibUtil.createFactory(this);
	}

	@Override
	protected void defineSynchedData() 
	{
		this.entityData.define(IS_BIG, false);
	}
	
	public void setBig(boolean value)
	{
		this.entityData.set(IS_BIG, value);
	}
	
	public boolean isBig()
	{
		return this.entityData.get(IS_BIG);
	}
}
