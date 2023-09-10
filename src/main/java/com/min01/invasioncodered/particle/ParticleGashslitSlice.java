package com.min01.invasioncodered.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class ParticleGashslitSlice extends SimpleAnimatedParticle
{
	private float fadeR;
	private float fadeG;
	private float fadeB;
	private boolean hasFade;
	
	protected ParticleGashslitSlice(ClientLevel p_107647_, double p_107648_, double p_107649_, double p_107650_, double p_106706_, double p_106707_, double p_106708_, SpriteSet p_107651_, float p_107652_) 
	{
		super(p_107647_, p_107648_, p_107649_, p_107650_, p_107651_, p_107652_);
        this.xd = p_106706_;
        this.yd = p_106707_;
        this.zd = p_106708_;
        this.quadSize *= 0.75F;
        this.lifetime = 4;
        this.setSpriteFromAge(p_107651_);
        this.setColor(16754717);
        this.setFadeColor(16711680);
	}
	
	@Override
	public void setFadeColor(int p_107660_)
	{
		this.fadeR = (float)((p_107660_ & 16711680) >> 16) / 255.0F;
		this.fadeG = (float)((p_107660_ & '\uff00') >> 8) / 255.0F;
		this.fadeB = (float)((p_107660_ & 255) >> 0) / 255.0F;
		this.hasFade = true;
	}
	
	@Override
	public void tick() 
	{     
		this.setSpriteFromAge(this.sprites);
		if (this.age / this.lifetime == 0)
		{
			if (this.hasFade)
			{
				this.rCol += (this.fadeR - this.rCol) * 0.2F;
				this.gCol += (this.fadeG - this.gCol) * 0.2F;
				this.bCol += (this.fadeB - this.bCol) * 0.2F;
			}
		}
		
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.age++ >= this.lifetime)
		{
			this.remove();
		}
		else
		{
			this.yd -= 0.04D * (double)this.gravity;
			this.move(this.xd, this.yd, this.zd);
			if (this.speedUpWhenYMotionIsBlocked && this.y == this.yo)
			{
				this.xd *= 1.1D;
				this.zd *= 1.1D;
			}

			this.xd *= (double)this.friction;
			this.yd *= (double)this.friction;
			this.zd *= (double)this.friction;
			if (this.onGround) 
			{
				this.xd *= (double)0.7F;
				this.zd *= (double)0.7F;
			}
		}
	}
	
	public static class Provider implements ParticleProvider<SimpleParticleType> 
	{
		private final SpriteSet sprites;

		public Provider(SpriteSet p_106733_)
		{
			this.sprites = p_106733_;
		}

		@Override
		public Particle createParticle(SimpleParticleType p_107421_, ClientLevel p_107422_, double p_107423_, double p_107424_, double p_107425_, double p_107426_, double p_107427_, double p_107428_)
		{
			return new ParticleGashslitSlice(p_107422_, p_107423_, p_107424_, p_107425_, p_107426_, p_107427_, p_107428_, this.sprites, 0);
		}
	}
}
