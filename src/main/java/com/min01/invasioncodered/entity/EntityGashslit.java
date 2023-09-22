package com.min01.invasioncodered.entity;

import java.util.Iterator;
import java.util.List;

import com.min01.invasioncodered.misc.ICRUtil;
import com.min01.invasioncodered.network.GashslitParticlePacket;
import com.min01.invasioncodered.network.GashslitParticlePacket.ParticleType;
import com.min01.invasioncodered.network.ICRNetwork;
import com.min01.invasioncodered.particle.ICRParticles;
import com.min01.invasioncodered.sound.ICRSounds;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class EntityGashslit extends AbstractBedrockRaider implements IAnimatable
{ 
	protected static final AnimationBuilder WALK = new AnimationBuilder().addAnimation("animation.gashslit.walk", ILoopType.EDefaultLoopTypes.LOOP);
	protected static final AnimationBuilder RUN = new AnimationBuilder().addAnimation("animation.gashslit.run", ILoopType.EDefaultLoopTypes.LOOP);
	protected static final AnimationBuilder PREPARE = new AnimationBuilder().addAnimation("animation.gashslit.prepare", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
	protected static final AnimationBuilder CHARGE = new AnimationBuilder().addAnimation("animation.gashslit.charge", ILoopType.EDefaultLoopTypes.LOOP);
	protected static final AnimationBuilder DASH_POSE = new AnimationBuilder().addAnimation("animation.gashslit.flew2", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
	protected static final AnimationBuilder SLOW_ATTACK = new AnimationBuilder().addAnimation("animation.gashslit.slowattack", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
	protected static final AnimationBuilder ATTACK = new AnimationBuilder().addAnimation("animation.gashslit.attack", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
	protected static final AnimationBuilder SMASH = new AnimationBuilder().addAnimation("animation.gashslit.swordsmash", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
	protected static final AnimationBuilder BLOCK = new AnimationBuilder().addAnimation("animation.gashslit.block", ILoopType.EDefaultLoopTypes.LOOP);
	protected static final AnimationBuilder STEP_BACK = new AnimationBuilder().addAnimation("animation.gashslit.stepback", ILoopType.EDefaultLoopTypes.LOOP);
	protected static final AnimationBuilder SHOOT = new AnimationBuilder().addAnimation("animation.gashslit.shoot", ILoopType.EDefaultLoopTypes.LOOP);
	protected static final AnimationBuilder FLEX = new AnimationBuilder().addAnimation("animation.gashslit.flex", ILoopType.EDefaultLoopTypes.LOOP);
	protected static final AnimationBuilder DEATH = new AnimationBuilder().addAnimation("animation.gashslit.death", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME);
	protected static final AnimationBuilder RAGE_POSE = new AnimationBuilder().addAnimation("animation.gashslit.ragepose", ILoopType.EDefaultLoopTypes.LOOP);
	
	private static final EntityDataAccessor<Boolean> POWER = SynchedEntityData.defineId(EntityGashslit.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> DEFENSE = SynchedEntityData.defineId(EntityGashslit.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> POWER2 = SynchedEntityData.defineId(EntityGashslit.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> LAST_CHANCE = SynchedEntityData.defineId(EntityGashslit.class, EntityDataSerializers.BOOLEAN);

	private double speed = 0.24D;
	private int deathAnimTime;
	
    private List<TimedEvent> events = new ObjectArrayList<>();
    private List<TimedEvent> fired = new ObjectArrayList<>();
    private List<EntityGashslitDragon> summonCap = new ObjectArrayList<>();
	private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
	private final ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PINK, BossEvent.BossBarOverlay.PROGRESS));
	
	public EntityGashslit(EntityType<? extends Raider> p_33002_, Level p_33003_) 
	{
		super(p_33002_, p_33003_);
	}
	
    public static AttributeSupplier.Builder createAttributes()
    {
        return Mob.createMobAttributes()
    			.add(Attributes.MAX_HEALTH, 680)
    			.add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
    			.add(Attributes.MOVEMENT_SPEED, 0.24D)
        		.add(Attributes.ATTACK_DAMAGE, 13)
        		.add(Attributes.FOLLOW_RANGE, 100);
    }
    
    @Override
    protected void defineSynchedData() 
    {
    	super.defineSynchedData();
    	this.entityData.define(POWER, false);
    	this.entityData.define(DEFENSE, false);
    	this.entityData.define(POWER2, false);
    	this.entityData.define(LAST_CHANCE, false);
    }
    
	@Override
	protected void tickDeath()
	{
		++this.deathAnimTime;
		if(this.deathAnimTime >= 95)
		{
			this.level.broadcastEntityEvent(this, (byte) 1);
			this.setRemoved(RemovalReason.KILLED);
		}
	}
    
    @Override
    protected void playStepSound(BlockPos p_20135_, BlockState p_20136_)
    {
    	this.playSound(SoundEvents.WARDEN_STEP);
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource p_33034_) 
    {
    	return ICRSounds.GASHSLIT_HURT.get();
    }
    
    @Override
    protected SoundEvent getAmbientSound() 
    {
    	return ICRSounds.GASHSLIT_AMBIENT.get();
    }
    
    @Override
    protected void registerGoals()
    {
    	this.goalSelector.addGoal(0, new FloatGoal(this));
    	this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Monster.class, 8.0F));
    	this.goalSelector.addGoal(7, new RandomStrollGoal(this, 0.60));
    	this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    	this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    	this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true, (living) ->
    	{
    		return !(living instanceof EntityGashslitDragon) && !(living instanceof EntityGashslit);
    	}));
    	this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
    	this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    	this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, SnowGolem.class, true));
    	this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true, (villager) ->
    	{
    		return !villager.isBaby();
    	}));
    }
    
    @Override
    protected void customServerAiStep()
    {
    	super.customServerAiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }
    
    @Override
    public void startSeenByPlayer(ServerPlayer p_31483_) 
    {
    	super.startSeenByPlayer(p_31483_);
    	this.bossEvent.addPlayer(p_31483_);
    }
    
    @Override
    public void stopSeenByPlayer(ServerPlayer p_31488_)
    {
    	super.stopSeenByPlayer(p_31488_);
    	this.bossEvent.removePlayer(p_31488_);
    }
    
    @Override
    public void tick() 
    {
    	super.tick();
    	this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, 9, true, false));
    	if(this.isAlive())
    	{
    		if(this.getTarget() != null)
    		{
    			if(this.getSkinID() != 9)
    			{
    	    		if(!this.isEventFired() && this.getSkinID() == 0)
    	    		{
    	    			if(this.getHealth() <= 300)
    	    			{
    	    				this.phase2RandomAttack();
    	    			}
    	    			else
    	    			{
    	        			this.randomAttack();
    	    			}
    	    		}
    	    		
    	    		if(this.isDelayedAttacking())
    	    		{
    	        		//this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, 9, true, false));
    	    		}
    	    		
    	    		if(this.getHealth() <= 520 && !this.entityData.get(POWER))
    	    		{
    	        		this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 0, true, false));
    	        		this.entityData.set(POWER, true);
    	    		}
    	    		else if(this.getHealth() <= 300 && !this.entityData.get(DEFENSE))
    	    		{
    		    		this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2400, 2, true, false));
    		    		this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 780, 1, true, false));
    	        		ICRNetwork.sendToAll(new GashslitParticlePacket(ParticleType.POP_EFFECT, this));
    	        		this.entityData.set(DEFENSE, true);
    	    		}
    	    		else if(this.getHealth() <= 150 && !this.entityData.get(POWER2))
    	    		{
    	        		this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 199980, 2, true, false));
    	        		this.entityData.set(POWER2, true);
    	    		}
    	    		else if(this.getHealth() <= 10 && !this.entityData.get(LAST_CHANCE))
    	    		{
    	           		this.removeAllEffects();
    	        		this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 4, true, false));
    	        		this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 2, true, false));
    	        		ICRNetwork.sendToAll(new GashslitParticlePacket(ParticleType.POP_EFFECT, this));
    	        		this.entityData.set(LAST_CHANCE, true);
    	    		}
    			}
    			
        		this.lookControl.setLookAt(this.getTarget(), 30, 30);
        		this.getNavigation().moveTo(this.getTarget(), this.getAttributeBaseValue(Attributes.MOVEMENT_SPEED));
        		
        		for(int i = 0; i < this.events.size(); i++)
        		{
        			TimedEvent event = this.events.get(i);
               		if (event != null && event.ticks <= this.tickCount) 
            		{
            			event.callback.run();
            			this.fired.add(event);
            		}
        		}
        		for(Iterator<TimedEvent> itr = this.events.iterator(); itr.hasNext();)
        		{
        			TimedEvent event = itr.next();
               		if (event != null && this.fired.contains(event)) 
            		{
            			itr.remove();
            		} 
        		}
        		
        		for(Iterator<EntityGashslitDragon> itr = this.summonCap.iterator(); itr.hasNext();)
        		{
        			EntityGashslitDragon dragon = itr.next();
               		if (dragon != null && !dragon.isAlive()) 
            		{
            			itr.remove();
            		}
        		}
        		
            	if(this.getSkinID() == 6)
            	{
                	if(this.tickCount % 5 == 0)
                	{
                		EntityRangeSlash slash = new EntityRangeSlash(ICREntities.RANGE_SLASH.get(), this.level);
                		slash.setOwner(this);
                		slash.setPos(this.getX(), this.getEyeY() - 0.5, this.getZ());
                		if(this.getHealth() <= 300)
                		{
                			slash.setBig(true);
                		}
                		double d0 = this.getTarget().getX() - this.getX();
                		double d1 = this.getTarget().getY(0.5) - slash.getY();
                		double d2 = this.getTarget().getZ() - this.getZ();
                		double d3 = Math.sqrt(d0 * d0 + d2 * d2);
                		slash.shoot(d0 * 3, d1 + d3 * (double)0.0F, d2 * 3, 1.6F, 1);
                		this.level.addFreshEntity(slash);
                	}
            	}
            	
            	if(this.getSkinID() == 9)
            	{
            		List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(3.5D));
            		for(int i = 0; i < list.size(); i++)
            		{
            			LivingEntity living = list.get(i);
            			if(living != this)
            			{
            				living.hurt(DamageSource.mobAttack(this), 46);
            			}
            		}
            		
            		if(!this.level.isClientSide)
            		{
                        ICRNetwork.sendToAll(new GashslitParticlePacket(ParticleType.SLASH_HIT, this));
            		}
            	}
            	
            	if(this.getSkinID() == 8)
            	{
        			float f14 = this.getYRot() * ((float)Math.PI / 180F);
        	        float x = Mth.sin(f14);
        	        float z = Mth.cos(f14);
        	        this.setPos(this.getX() + (x * -6), this.getY() + 0.2, this.getZ() + (z * 6));
        	        this.setYRot(-this.getYRot());
        	        if(!this.level.isClientSide)
        	        {
        	            ICRNetwork.sendToAll(new GashslitParticlePacket(ParticleType.SLASH_HIT, this));
        	        }
        			List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(2.5D));
        			for(int i = 0; i < list.size(); i++)
        			{
        				LivingEntity living = list.get(i);
        				if(living != this)
        				{
        					living.hurt(DamageSource.mobAttack(this), this.getHealth() <= 300 ? 25 : 20);
        				}
        			}
            	}
    		}
    		
        	if(this.getHealth() <= 300)
        	{
    			for(int i = 0; i < 50; i ++)
    			{
    	    		double spawnRange = 30;
    	    		double yRange = 16;
    	            double x = (double) this.getX() + (this.level.random.nextDouble() - this.level.random.nextDouble()) * (double)spawnRange + 0.5D;
    	            double y = (double) this.getY() + (this.level.random.nextDouble() - this.level.random.nextDouble()) * (double)yRange + 0.5D;
    	            double z = (double) this.getZ() + (this.level.random.nextDouble() - this.level.random.nextDouble()) * (double)spawnRange + 0.5D;
    	            this.level.addParticle(ICRParticles.RAGE_MODE.get(), x, y, z, 0, 0.2, 0);
    			}
        	}
    	}
    	
    	if(!this.isAlive())
    	{
    		this.removeAllEvents();
    	}
    	
    	if(!this.level.isClientSide)
    	{
    		if(this.getTarget() == null)
    		{
    			this.removeAllEvents();	
    		}
    	}
    }
    
    public void randomAttack()
    {
    	if(ICRUtil.percent(0.09))
    	{
			if(this.isWithinMeleeAttackRange(this.getTarget(), 3.9F))
			{
	    		this.walkmode1();
			}
    	}
    	else if(ICRUtil.percent(0.1))
    	{
			if(this.isWithinMeleeAttackRange(this.getTarget(), 2.5F))
			{
				this.walkmode2();
			}
    	}  	
    	else if(ICRUtil.percent(0.08))
    	{
			if(this.isWithinMeleeAttackRange(this.getTarget(), 3.9F))
			{
				this.smash();
			}
    	}
    	else if(ICRUtil.percent(0.2))
    	{
			if(this.isWithinMeleeAttackRange(this.getTarget(), 3.9F) || this.tickCount % 120 == 0)
			{
    			this.dash2start();
    		}
    	}
    	else if(ICRUtil.percent(0.08))
    	{
			if(this.isWithinMeleeAttackRange(this.getTarget(), 3.9F))
			{
	    		this.blockmode();
			}
    	}
    	else if(ICRUtil.percent(0.09))
    	{
			if(this.isWithinMeleeAttackRange(this.getTarget(), 3.9F) || this.tickCount % 120 == 0)
			{
				this.stepback();
			}
    	}
    	else if(ICRUtil.percent(0.12))
    	{
			if(this.isWithinMeleeAttackRange(this.getTarget(), 4.5F) || this.tickCount % 120 == 0)
			{
	    		this.dash();
			}
    	}
    	else if(ICRUtil.percent(0.08))
    	{
			if(this.isWithinMeleeAttackRange(this.getTarget(), 4.5F) || this.tickCount % 120 == 0)
			{
	    		if(this.summonCap.size() < 8)
	    		{
	    			this.summonmode();
	    		}
			}
    	}
    }
    
    public void phase2RandomAttack()
    {
    	if(ICRUtil.percent(0.09))
    	{
    		if(this.isWithinMeleeAttackRange(this.getTarget(), 5.9F))
			{
	    		this.phase2walkmode1();
			}
    	}
    	else if(ICRUtil.percent(0.1))
    	{
			if(this.isWithinMeleeAttackRange(this.getTarget(), 4.5F))
			{
				this.phase2walkmode2();
			}
    	}
    	else if(ICRUtil.percent(0.08))
    	{
			if(this.isWithinMeleeAttackRange(this.getTarget(), 5.9F))
			{
				this.phase2smash();
			}
    	}    	
    	else if(ICRUtil.percent(0.17))
    	{
			if(this.isWithinMeleeAttackRange(this.getTarget(), 3.9F) || this.tickCount % 120 == 0)
			{
    			this.phase2dash2start();
    		}
    	}
    	else if(ICRUtil.percent(0.08))
    	{
			if(this.isWithinMeleeAttackRange(this.getTarget(), 3.9F))
			{
	    		this.phase2blockmode();
			}
    	}
    	else if(ICRUtil.percent(0.09))
    	{
			if(this.isWithinMeleeAttackRange(this.getTarget(), 3.9F) || this.tickCount % 120 == 0)
			{
				this.phase2stepback();
			}
    	}
    	else if(ICRUtil.percent(0.12))
    	{
			if(this.isWithinMeleeAttackRange(this.getTarget(), 4.5F) || this.tickCount % 120 == 0)
			{
	    		this.phase2dash();
			}
    	}
    }
    
    public void phase2dash()
    {
    	this.setEventFired(true);
    	this.setSkinID(8);
    	this.addEvent(new Runnable() 
    	{
			@Override
			public void run() 
			{
				EntityGashslit.this.setEventFired(false);
				EntityGashslit.this.setSkinID(0);
			}
		}, 24);
    }
    
    public void phase2shootmode()
    {
    	this.setSkinID(6);    	
    	this.addEvent(new Runnable() 
    	{
			@Override
			public void run() 
			{
				EntityGashslit.this.setEventFired(false);
				EntityGashslit.this.setSkinID(0);
			}
		}, 120);
    }
    
    public void phase2stepback()
    {
    	this.setEventFired(true);
    	this.setSkinID(7);
        double d0 = this.getX() - this.getTarget().getX();
		double d2 = this.getZ() - this.getTarget().getZ();
		double xD = -d0 / (0.2f * this.distanceTo(this.getTarget()));
		double zD = -d2 / (0.2f * this.distanceTo(this.getTarget()));
		this.setDeltaMovement(-xD, this.getDeltaMovement().y, -zD);
    	this.addEvent(() -> this.phase2shootmode(), 6);
    }
    
    public void phase2blockmode()
    {
    	this.setEventFired(true);
    	this.setSkinID(4);
    	this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0);
		this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(100);
    	this.setInvulnerable(true);
    	this.addEvent(new Runnable() 
    	{
			@Override
			public void run() 
			{
				EntityGashslit.this.setInvulnerable(false);
				EntityGashslit.this.setEventFired(false);
				EntityGashslit.this.setSkinID(0);
				EntityGashslit.this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(EntityGashslit.this.speed);
				EntityGashslit.this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0.5);
			}
		}, 120);
    }

    public void phase2dash2()
    {
    	this.setSkinID(9);
    	this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0);
        double d0 = this.getX() - this.getTarget().getX();
		double d2 = this.getZ() - this.getTarget().getZ();
		double xD = -d0 / (0.07f * this.distanceTo(this.getTarget()));
		double zD = -d2 / (0.07f * this.distanceTo(this.getTarget()));
		this.setDeltaMovement(xD, this.getDeltaMovement().y, zD);
		this.addEvent(new Runnable() 
		{
			@Override
			public void run()
			{
				EntityGashslit.this.setSkinID(0);
				EntityGashslit.this.setEventFired(false);
				EntityGashslit.this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(EntityGashslit.this.speed);
			}
		}, 8);
		this.playSound(ICRSounds.SLICE_FX1.get());
        if(!this.level.isClientSide)
        {
            ICRNetwork.sendToAll(new GashslitParticlePacket(ParticleType.DASH_SMOKE, this));
    		ICRNetwork.sendToAll(new GashslitParticlePacket(ParticleType.DASH_TRAIL, this));
        }
    }
    
    public void phase2dash2start()
    {
    	this.setEventFired(true);
    	this.setSkinID(10);
		this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.1);
    	this.addEvent(() -> this.phase2dash2(), 14);
        if(!this.level.isClientSide)
        {
    		ICRNetwork.sendToAll(new GashslitParticlePacket(ParticleType.DASH_TRAIL, this));
        }
    }
    
    public void phase2smash()
    {
    	this.setEventFired(true);
    	this.setSkinID(3);
    	this.setDelayedAttacking(true);
		this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.19);
		this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1.5);
    	this.addEvent(new Runnable() 
    	{
			@Override
			public void run() 
			{
				if(EntityGashslit.this.isWithinMeleeAttackRange(EntityGashslit.this.getTarget(), 5.9F))
				{
					EntityGashslit.this.getTarget().hurt(DamageSource.mobAttack(EntityGashslit.this), 29);
				}
			}
		}, 25);
    	this.addEvent(new Runnable() 
    	{
			@Override
			public void run() 
			{
				EntityGashslit.this.setDelayedAttacking(false);
				EntityGashslit.this.setEventFired(false);
				EntityGashslit.this.setSkinID(0);
				EntityGashslit.this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(EntityGashslit.this.speed);
				EntityGashslit.this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0.5);
			}
		}, 50);
    }
    
    public void phase2walkmode2()
    {
    	this.setEventFired(true);
    	this.setSkinID(1);
    	this.setDelayedAttacking(true);
    	this.addEvent(new Runnable() 
    	{
			@Override
			public void run() 
			{
				if(EntityGashslit.this.isWithinMeleeAttackRange(EntityGashslit.this.getTarget(), 4.5F))
				{
					EntityGashslit.this.getTarget().hurt(DamageSource.mobAttack(EntityGashslit.this), 13);
				}
			}
		}, 7);
    	this.addEvent(new Runnable()
    	{
			@Override
			public void run() 
			{
				EntityGashslit.this.setDelayedAttacking(false);
				EntityGashslit.this.setEventFired(false);
				EntityGashslit.this.setSkinID(0);
			}
		}, 15);
    }
    
    public void phase2walkmode1()
    {
    	this.setEventFired(true);
    	this.setSkinID(2);
    	this.setDelayedAttacking(true);
    	this.addEvent(new Runnable() 
    	{
			@Override
			public void run() 
			{
				if(EntityGashslit.this.isWithinMeleeAttackRange(EntityGashslit.this.getTarget(), 5.9F))
				{
					EntityGashslit.this.getTarget().hurt(DamageSource.mobAttack(EntityGashslit.this), 25);
				}
			}
		}, 20);
    	this.addEvent(new Runnable()
    	{
			@Override
			public void run() 
			{
				EntityGashslit.this.setDelayedAttacking(false);
				EntityGashslit.this.setEventFired(false);
				EntityGashslit.this.setSkinID(0);
			}
		}, 40);
    }
    
    public void summonmode()
    {
    	this.setEventFired(true);
    	this.setSkinID(5);
    	this.addEvent(new Runnable()
    	{
			@Override
			public void run()
			{
		    	for(int i = 0; i < 4; i++)
		    	{
		        	EntityGashslitDragon dragon = new EntityGashslitDragon(ICREntities.GASHSLIT_DRAGON.get(), EntityGashslit.this.level);
		        	dragon.setPos(EntityGashslit.this.getX() + EntityGashslit.this.level.random.nextGaussian() * 0.2D, EntityGashslit.this.getY() + 0.5 + EntityGashslit.this.level.random.nextGaussian() * 0.2D, EntityGashslit.this.getZ() + EntityGashslit.this.level.random.nextGaussian() * 0.2D);
		        	dragon.setLimitedLife(780);
		        	EntityGashslit.this.level.addFreshEntity(dragon);
		        	EntityGashslit.this.summonCap.add(dragon);
		    	}
			}
		}, 20);
    	this.addEvent(new Runnable() 
    	{
			@Override
			public void run() 
			{
				EntityGashslit.this.setEventFired(false);
				EntityGashslit.this.setSkinID(0);
			}
		}, 50);
    }
    
    //slice barrage
    public void dash()
    {
    	this.setEventFired(true);
    	this.setSkinID(8);
    	this.addEvent(new Runnable() 
    	{
			@Override
			public void run() 
			{
				EntityGashslit.this.setEventFired(false);
				EntityGashslit.this.setSkinID(0);
			}
		}, 24);
    }
    
    public void shootmode()
    {
    	this.setSkinID(6);    	
    	this.addEvent(new Runnable() 
    	{
			@Override
			public void run() 
			{
				EntityGashslit.this.setEventFired(false);
				EntityGashslit.this.setSkinID(0);
			}
		}, 120);
    }
    
    public void stepback()
    {
    	this.setEventFired(true);
    	this.setSkinID(7);
        double d0 = this.getX() - this.getTarget().getX();
		double d2 = this.getZ() - this.getTarget().getZ();
		double xD = -d0 / (0.2f * this.distanceTo(this.getTarget()));
		double zD = -d2 / (0.2f * this.distanceTo(this.getTarget()));
		this.setDeltaMovement(-xD, this.getDeltaMovement().y, -zD);
    	this.addEvent(() -> this.shootmode(), 6);
    }
    
    public void blockmode()
    {
    	this.setEventFired(true);
    	this.setSkinID(4);
    	this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0);
		this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(100);
    	this.addEvent(new Runnable() 
    	{
			@Override
			public void run() 
			{
				EntityGashslit.this.setEventFired(false);
				EntityGashslit.this.setSkinID(0);
				EntityGashslit.this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(EntityGashslit.this.speed);
				EntityGashslit.this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0.5);
			}
		}, 120);
    }
    
    public void dash2()
    {
    	this.setSkinID(9);
    	this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0);
        double d0 = this.getX() - this.getTarget().getX();
		double d2 = this.getZ() - this.getTarget().getZ();
		double xD = -d0 / (0.07f * this.distanceTo(this.getTarget()));
		double zD = -d2 / (0.07f * this.distanceTo(this.getTarget()));
		this.setDeltaMovement(xD, this.getDeltaMovement().y, zD);
		this.addEvent(new Runnable() 
		{
			@Override
			public void run()
			{
				EntityGashslit.this.setSkinID(0);
				EntityGashslit.this.setEventFired(false);
				EntityGashslit.this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(EntityGashslit.this.speed);
			}
		}, 8);
		this.playSound(ICRSounds.SLICE_FX1.get());
        if(!this.level.isClientSide)
        {
            ICRNetwork.sendToAll(new GashslitParticlePacket(ParticleType.DASH_SMOKE, this));
    		ICRNetwork.sendToAll(new GashslitParticlePacket(ParticleType.DASH_TRAIL, this));
        }
    }
    
    public void dash2start()
    {
    	this.setEventFired(true);
    	this.setSkinID(10);
		this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.19);
    	this.addEvent(() -> this.dash2(), 14);
        if(!this.level.isClientSide)
        {
    		ICRNetwork.sendToAll(new GashslitParticlePacket(ParticleType.DASH_TRAIL, this));
        }
    }
    
    public void smash()
    {
    	this.setEventFired(true);
    	this.setSkinID(3);
    	this.setDelayedAttacking(true);
		this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.19);
    	this.addEvent(new Runnable() 
    	{
			@Override
			public void run() 
			{
				if(EntityGashslit.this.isWithinMeleeAttackRange(EntityGashslit.this.getTarget(), 3.9F))
				{
					EntityGashslit.this.getTarget().hurt(DamageSource.mobAttack(EntityGashslit.this), 29);
				}
			}
		}, 25);
    	this.addEvent(new Runnable() 
    	{
			@Override
			public void run() 
			{
				EntityGashslit.this.setDelayedAttacking(false);
				EntityGashslit.this.setEventFired(false);
				EntityGashslit.this.setSkinID(0);
				EntityGashslit.this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(EntityGashslit.this.speed);
			}
		}, 50);
    }
    
    public void walkmode2()
    {
    	this.setEventFired(true);
    	this.setSkinID(1);
    	this.setDelayedAttacking(true);
    	this.addEvent(new Runnable() 
    	{
			@Override
			public void run() 
			{
				if(EntityGashslit.this.isWithinMeleeAttackRange(EntityGashslit.this.getTarget(), 2.5F))
				{
					EntityGashslit.this.getTarget().hurt(DamageSource.mobAttack(EntityGashslit.this), 13);
				}
			}
		}, 7);
    	this.addEvent(new Runnable()
    	{
			@Override
			public void run() 
			{
				EntityGashslit.this.setDelayedAttacking(false);
				EntityGashslit.this.setEventFired(false);
				EntityGashslit.this.setSkinID(0);
			}
		}, 15);
    }
    
    public void walkmode1()
    {
    	this.setEventFired(true);
    	this.setSkinID(2);
    	this.setDelayedAttacking(true);
    	this.addEvent(new Runnable() 
    	{
			@Override
			public void run() 
			{
				if(EntityGashslit.this.isWithinMeleeAttackRange(EntityGashslit.this.getTarget(), 3.9F))
				{
					EntityGashslit.this.getTarget().hurt(DamageSource.mobAttack(EntityGashslit.this), 25);
				}
			}
		}, 20);
    	this.addEvent(new Runnable()
    	{
			@Override
			public void run() 
			{
				EntityGashslit.this.setDelayedAttacking(false);
				EntityGashslit.this.setEventFired(false);
				EntityGashslit.this.setSkinID(0);
			}
		}, 40);
    }
    
    public void removeAllEvents()
    {
    	this.events.clear();
    	this.fired.clear();
		this.setDelayedAttacking(false);
		this.setEventFired(false);
		this.setSkinID(0);
    }
    
    public void addEvent(Runnable runnable, int ticksFromNow) 
    {
        this.events.add(new TimedEvent(runnable, this.tickCount + ticksFromNow));
    }

    private static class TimedEvent implements Comparable<TimedEvent> 
    {
        Runnable callback;
        int ticks;

        public TimedEvent(Runnable callback, int ticks) 
        {
            this.callback = callback;
            this.ticks = ticks;
        }

        @Override
        public int compareTo(TimedEvent event) 
        {
            return event.ticks < ticks ? 1 : -1;
        }
    }
    
    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) 
    {
    	if(this.getSkinID() == 4 && !p_21016_.isBypassInvul())
    	{
    		return false;
    	}
    	return super.hurt(p_21016_, p_21017_);
    }

	@Override
	public void registerControllers(AnimationData data) 
	{
		data.addAnimationController(new AnimationController<>(this, "attack", 1, this::attackController));
		data.addAnimationController(new AnimationController<>(this, "dash", 4, this::dashController));
		data.addAnimationController(new AnimationController<>(this, "block", 4, this::blockController));
		data.addAnimationController(new AnimationController<>(this, "move", 6, this::moveController));
		data.addAnimationController(new AnimationController<>(this, "stepback", 2, this::stepbackController));
		data.addAnimationController(new AnimationController<>(this, "shoot", 1, this::shootController));
		data.addAnimationController(new AnimationController<>(this, "summon", 4, this::summonController));
		data.addAnimationController(new AnimationController<>(this, "death", 1, this::deathController));
		data.addAnimationController(new AnimationController<>(this, "charge", 1, this::chargeController));
	}
	
    protected PlayState chargeController(AnimationEvent<EntityGashslit> event) 
    {
    	if(this.getSkinID() == 8 && this.isAlive())
    	{
    		event.getController().setAnimation(CHARGE);
    		return PlayState.CONTINUE;
    	}
    	return PlayState.STOP;
    }
	
    protected PlayState deathController(AnimationEvent<EntityGashslit> event) 
    {
    	if(!this.isAlive())
    	{
    		event.getController().setAnimation(DEATH);
    		return PlayState.CONTINUE;
    	}
    	return PlayState.STOP;
    }
	
    protected PlayState summonController(AnimationEvent<EntityGashslit> event) 
    {
    	if(this.getSkinID() == 5 && this.isAlive())
    	{
    		event.getController().setAnimation(FLEX);
    		return PlayState.CONTINUE;
    	}
    	return PlayState.STOP;
    }
	
    protected PlayState shootController(AnimationEvent<EntityGashslit> event) 
    {
    	if(this.getSkinID() == 6 && this.isAlive())
    	{
    		event.getController().setAnimation(SHOOT);
    		return PlayState.CONTINUE;
    	}
    	return PlayState.STOP;
    }
	
    protected PlayState stepbackController(AnimationEvent<EntityGashslit> event) 
    {
    	if(this.getSkinID() == 7 && this.isAlive())
    	{
    		event.getController().setAnimation(STEP_BACK);
    		return PlayState.CONTINUE;
    	}
    	return PlayState.STOP;
    }
	
    protected PlayState moveController(AnimationEvent<EntityGashslit> event) 
    {
    	if(this.isOnGround() && this.getSkinID() == 0 && this.isAlive())
    	{
			if(event.isMoving())
			{
        		if(this.getHealth() <= 300)
        		{
            		event.getController().setAnimation(RAGE_POSE);
        		}
        		else
        		{
            		if(this.getAttributeBaseValue(Attributes.MOVEMENT_SPEED) >= 0.98)
            		{
                		event.getController().setAnimation(RUN);
            		}
            		else if(this.getAttributeBaseValue(Attributes.MOVEMENT_SPEED) <= 0.2)
            		{
                		event.getController().setAnimation(WALK);
            		}
        		}
			}
    		else
    		{
        		if(this.getHealth() <= 300)
        		{
            		event.getController().setAnimation(RAGE_POSE);
        		}
    		}
    		return PlayState.CONTINUE;
    	}
    	return PlayState.STOP;
    }
	
    protected PlayState blockController(AnimationEvent<EntityGashslit> event) 
    {
    	if(this.getSkinID() == 4 && this.isAlive())
    	{
    		event.getController().setAnimation(BLOCK);
    		return PlayState.CONTINUE;
    	}
    	return PlayState.STOP;
    }
	
    protected PlayState dashController(AnimationEvent<EntityGashslit> event) 
    {
    	if(this.getSkinID() == 10 && this.isAlive())
    	{
    		event.getController().setAnimation(PREPARE);
    		return PlayState.CONTINUE;
    	}
    	else if(this.getSkinID() == 9 && this.isAlive())
    	{
    		event.getController().setAnimation(DASH_POSE);
    		return PlayState.CONTINUE;
    	}
		return PlayState.STOP;
    }
	
    protected PlayState attackController(AnimationEvent<EntityGashslit> event) 
    {
    	if(this.isDelayedAttacking() && this.getSkinID() == 1 && this.isAlive())
    	{
    		event.getController().setAnimation(ATTACK);
    		return PlayState.CONTINUE;
		}
    	else if(this.isDelayedAttacking() && this.getSkinID() == 2 && this.isAlive())
    	{
    		event.getController().setAnimation(SLOW_ATTACK);
    		return PlayState.CONTINUE;
    	}
    	else if(this.isDelayedAttacking() && this.getSkinID() == 3 && this.isAlive())
    	{
    		event.getController().setAnimation(SMASH);
    		return PlayState.CONTINUE;
    	}
		return PlayState.STOP;
    }
	
	@Override
	public AnimationFactory getFactory()
	{
		return this.factory;
	}

	@Override
	public void applyRaidBuffs(int p_37844_, boolean p_37845_) 
	{
		
	}

	@Override
	public SoundEvent getCelebrateSound()
	{
		return SoundEvents.VINDICATOR_CELEBRATE;
	}
}
