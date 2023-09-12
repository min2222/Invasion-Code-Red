package com.min01.invasioncodered.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class ParticleSlashHit extends SimpleAnimatedParticle
{
	private float fadeR;
	private float fadeG;
	private float fadeB;
	private boolean hasFade;
	
	protected ParticleSlashHit(ClientLevel p_107647_, double p_107648_, double p_107649_, double p_107650_, double p_106706_, double p_106707_, double p_106708_, SpriteSet p_107651_, float p_107652_) 
	{
		super(p_107647_, p_107648_, p_107649_, p_107650_, p_107651_, p_107652_);
        this.xd = p_106706_;
        this.yd = p_106707_;
        this.zd = p_106708_;
        this.scale(6);
        this.lifetime = 20;
        this.setSpriteFromAge(p_107651_);
        this.roll = (float)Math.random() * ((float)Math.PI * 2F);
        this.setColor(16777215);
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
	
	@Override
	public void render(VertexConsumer p_107678_, Camera p_107679_, float p_107680_)
	{
		Vec3 vec3 = p_107679_.getPosition();
		float f = (float)(Mth.lerp((double)p_107680_, this.xo, this.x) - vec3.x());
		float f1 = (float)(Mth.lerp((double)p_107680_, this.yo, this.y) - vec3.y());
		float f2 = (float)(Mth.lerp((double)p_107680_, this.zo, this.z) - vec3.z());
		Quaternion quaternion;
		if (this.roll == 0.0F) 
		{
			quaternion = p_107679_.rotation();
		}
		else 
		{
			quaternion = new Quaternion(p_107679_.rotation());
			float f3 = this.roll;
			quaternion.mul(Vector3f.ZP.rotation(f3));
		}
		
		Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
		vector3f1.transform(quaternion);
		Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
		float f4 = this.getQuadSize(p_107680_);
		
		for(int i = 0; i < 4; ++i) 
		{
			Vector3f vector3f = avector3f[i];
			vector3f.transform(quaternion);
			vector3f.mul(f4);
			vector3f.add(f, f1, f2);
		}

		float f7 = this.getU0();
		float f8 = this.getU1();
		float f5 = this.getV0();
		float f6 = this.getV1();
		int j = this.getLightColor(p_107680_);
		p_107678_.vertex((double)avector3f[0].x(), (double)avector3f[0].y(), (double)avector3f[0].z()).uv(f8, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
		p_107678_.vertex((double)avector3f[1].x(), (double)avector3f[1].y(), (double)avector3f[1].z()).uv(f8, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
		p_107678_.vertex((double)avector3f[2].x(), (double)avector3f[2].y(), (double)avector3f[2].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
		p_107678_.vertex((double)avector3f[3].x(), (double)avector3f[3].y(), (double)avector3f[3].z()).uv(f7, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
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
			return new ParticleSlashHit(p_107422_, p_107423_, p_107424_, p_107425_, p_107426_, p_107427_, p_107428_, this.sprites, 0);
		}
	}
}
