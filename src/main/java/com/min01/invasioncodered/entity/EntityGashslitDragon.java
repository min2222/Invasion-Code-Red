package com.min01.invasioncodered.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class EntityGashslitDragon extends Monster implements IAnimatable, RangedAttackMob
{
	protected static final AnimationBuilder SHOOT = new AnimationBuilder().addAnimation("animation.gashslitdragon.shoot", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
	protected static final AnimationBuilder FLY = new AnimationBuilder().addAnimation("animation.gashslitdragon.fly", ILoopType.EDefaultLoopTypes.LOOP);
	private static final EntityDataAccessor<Boolean> IS_HEALER = SynchedEntityData.defineId(EntityGashslit.class, EntityDataSerializers.BOOLEAN);
	private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
	private boolean hasLimitedLife;
	private int limitedLifeTicks;
	
	public EntityGashslitDragon(EntityType<? extends Monster> p_33002_, Level p_33003_) 
	{
		super(p_33002_, p_33003_);
		this.moveControl = new EntityGashslitDragon.DragonMoveControl(this);
		this.xpReward = 3;
	}
	
	@Override
	protected PathNavigation createNavigation(Level p_218342_)
	{
		FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, p_218342_);
		flyingpathnavigation.setCanOpenDoors(false);
		flyingpathnavigation.setCanFloat(true);
		flyingpathnavigation.setCanPassDoors(true);
		return flyingpathnavigation;
	}
	
    public static AttributeSupplier.Builder createAttributes()
    {
        return Mob.createMobAttributes()
        		.add(Attributes.MAX_HEALTH, 10)
        		.add(Attributes.MOVEMENT_SPEED, 2)
        		.add(Attributes.ATTACK_DAMAGE, 6)
        		.add(Attributes.FOLLOW_RANGE, 100);
    }
    
    @Override
    public void tick()
    {
    	super.tick();
        this.setNoGravity(true);
        if (this.hasLimitedLife && --this.limitedLifeTicks <= 0) 
        {
        	this.limitedLifeTicks = 20;
        	this.hurt(DamageSource.STARVE, 1.0F);
        }
        if(this.getTarget() != null)
        {
        	this.getNavigation().moveTo(this.getTarget(), 1);
        }
    }
    
    @Override
    protected void registerGoals() 
    {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
    	this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1, 20, 40, 36));
    	this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true, (living) ->
    	{
    		return !(living instanceof EntityGashslitDragon) && !(living instanceof EntityGashslit);
    	}));
    	this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    	this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    	this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true, (p_199899_) ->
    	{
            return !p_199899_.isBaby();
        }));
    }
    
    public void setLimitedLife(int p_33988_)
    {
    	this.hasLimitedLife = true;
        this.limitedLifeTicks = p_33988_;
    }
    
    @Override
    protected SoundEvent getAmbientSound() 
    {
        return SoundEvents.VEX_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
    	return SoundEvents.VEX_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_34023_) 
    {
    	return SoundEvents.VEX_HURT;
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag p_34008_) 
    {
        super.readAdditionalSaveData(p_34008_);
        if (p_34008_.contains("LifeTicks"))
        {
        	this.setLimitedLife(p_34008_.getInt("LifeTicks"));
        }
     }

    @Override
    public void addAdditionalSaveData(CompoundTag p_34015_)
    {
    	super.addAdditionalSaveData(p_34015_);
    	if (this.hasLimitedLife) 
    	{
    		p_34015_.putInt("LifeTicks", this.limitedLifeTicks);
    	}
    }
	
	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(IS_HEALER, false);
	}
	
	public void setHealer(boolean value)
	{
		this.entityData.set(IS_HEALER, value);
	}
	
	public boolean isHealer()
	{
		return this.entityData.get(IS_HEALER);
	}

	@Override
	public void registerControllers(AnimationData data)
	{
		data.addAnimationController(new AnimationController<>(this, "fly", 1, this::flyController));
		data.addAnimationController(new AnimationController<>(this, "shoot", 1, this::shootController));
	}

	@Override
	public AnimationFactory getFactory() 
	{
		return this.factory;
	}
	
    protected PlayState flyController(AnimationEvent<EntityGashslitDragon> event) 
    {
		event.getController().setAnimation(FLY);
		return PlayState.CONTINUE;
    }
    
    protected PlayState shootController(AnimationEvent<EntityGashslitDragon> event) 
    {
		event.getController().setAnimation(SHOOT);
		return PlayState.CONTINUE;
    }

	@Override
	public void performRangedAttack(LivingEntity p_33317_, float p_33318_)
	{
		SmallFireball fireball = new SmallFireball(this.level, this, this.getX(), this.getEyeY() - 0.5, this.getZ());
		double d0 = p_33317_.getX() - this.getX();
		double d1 = p_33317_.getY(0.3333333333333333D) - fireball.getY();
		double d2 = p_33317_.getZ() - this.getZ();
		double d3 = Math.sqrt(d0 * d0 + d2 * d2);
		fireball.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.level.getDifficulty().getId() * 4));
		this.level.addFreshEntity(fireball);
	}
	
	class DragonMoveControl extends MoveControl 
	{
		public DragonMoveControl(EntityGashslitDragon entityGashslitDragon)
		{
			super(entityGashslitDragon);
		}

		@Override
		public void tick() 
		{
			if (this.operation == MoveControl.Operation.MOVE_TO) 
			{
				Vec3 vec3 = new Vec3(this.wantedX - EntityGashslitDragon.this.getX(), this.wantedY - EntityGashslitDragon.this.getY(), this.wantedZ - EntityGashslitDragon.this.getZ());
				double d0 = vec3.length();
				if (d0 < EntityGashslitDragon.this.getBoundingBox().getSize()) 
				{
					this.operation = MoveControl.Operation.WAIT;
					EntityGashslitDragon.this.setDeltaMovement(EntityGashslitDragon.this.getDeltaMovement().scale(0.5D));
				} 
				else 
				{
					EntityGashslitDragon.this.setDeltaMovement(EntityGashslitDragon.this.getDeltaMovement().add(vec3.scale(this.speedModifier * 0.05D / d0)));
					if (EntityGashslitDragon.this.getTarget() == null) 
					{
						Vec3 vec31 = EntityGashslitDragon.this.getDeltaMovement();
						EntityGashslitDragon.this.setYRot(-((float)Mth.atan2(vec31.x, vec31.z)) * (180F / (float)Math.PI));
						EntityGashslitDragon.this.yBodyRot = EntityGashslitDragon.this.getYRot();
					} 
					else 
					{
						double d2 = EntityGashslitDragon.this.getTarget().getX() - EntityGashslitDragon.this.getX();
						double d1 = EntityGashslitDragon.this.getTarget().getZ() - EntityGashslitDragon.this.getZ();
						EntityGashslitDragon.this.setYRot(-((float)Mth.atan2(d2, d1)) * (180F / (float)Math.PI));
						EntityGashslitDragon.this.yBodyRot = EntityGashslitDragon.this.getYRot();
					}
				}
			}
		}
	}
}
