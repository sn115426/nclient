package com.ngames.nclient.baritone;

import static com.ngames.nclient.NClient.MC;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class BUtils
{
	public static double toDegrees(double Angle)
	{
		return Angle * 180 / Math.PI;
	}
	
	public static float toMinecraftDegrees(float angle)
	{
		if (angle > 180)
			return -(360 - angle);
		return angle;
	}
	
	/*public static float toNormalDegrees(float angle)
	{
		if (angle < 0)
			return angle + 360;
		return angle;
	}
	
	public static float calcYaw (double x1, double z1, double x2, double z2)
	{
	    double cos = (x1 * x2 + z1 * z2) / (Math.sqrt(x1 * x2 + z1 * z1) * Math.sqrt(x2 * x2 + z2 * z2));
	    return toMinecraftDegrees((float) toDegrees(Math.acos(cos)));
	}
	
	public static float calcYaw (double x, double z)
	{
		return calcYaw(MC.player.posX, MC.player.posZ, x, z);
	}
	
	public static float calcPitch (double y1, double x1, double x2, double y2)
	{
		double cos = (x1 * x2 + y1 * y2) / (Math.sqrt(x1 * x2 + y1 * y1) * Math.sqrt(x2 * x2 + y2 * y2));
	    return toMinecraftDegrees((float) toDegrees(Math.acos(cos)));
	}
	
	public static float calcPitch (double lc, double y)
	{
		double x2 = getLookingCoordinate();
		return calcPitch(x2, MC.player.posY, lc, y);
	}*/
	
	public static double getLookingCoordinate()
	{
		double ret = 0;
		Direction der = getDirection(false);
		if (der == Direction.Xneg || der == Direction.Xpos)
			ret = MC.player.posX;
		if (der == Direction.Zneg || der == Direction.Zpos)
			ret = MC.player.posZ;
		return ret;
	}
	
	public static double getLookingCoordinate (Entity entity) //получает X или Z сущности, в зависимости от направления, куда смотрит игрок
	{
		double ret = 0;
		Direction der = getDirection(false);
		if (der == Direction.Xneg || der == Direction.Xpos)
			ret = entity.posX;
		if (der == Direction.Zneg || der == Direction.Zpos)
			ret = entity.posZ;
		return ret;
	}
	
	public static List<Integer> genSimpleDelays(float cps)
	{
		List<Integer> ret = new ArrayList<>();
		int size = Math.round(cps);
		for (int i = 0; i < size; i++)
		{
			ret.add(Math.round(1000 / cps));
		}
		return ret;
	}
	public static List<Integer> genDelayNoise(float cps)
	{
		List<Integer> ret = new ArrayList<>();
		int size = Math.round(cps);
		for (int i = 0; i < size; i++)
		{
			int k = Math.round(1000 / cps);
			ret.add(randomInRange(Math.round(k - k * 0.30f), Math.round(k + k * 0.30f)));
		}
		return ret;
	}
	
	public static List<Integer> genAdvancedDelayNoise(float cps, int phase, boolean powered, float speed, float valueRange, 
			float minPhaseSize, float maxPhaseSize, float delayMlMin, float delayMlMax, short minLDelayChance, short maxLDelayChance)
	{
		List<Integer> ret = new ArrayList<>();
		int size = randomInRange(Math.round(minPhaseSize * cps), Math.round(maxPhaseSize * cps));
		boolean isDouble;
		int delay;
		boolean second = false;
		int prev = Math.round(1000 / cps);
		for (int i = 0; i < size; i++)
		{
			isDouble = random((short) 20);
			delay = Math.round(randomInRange(Math.round((speed / cps) * (1 - valueRange)), Math.round((speed / cps) * (1 + valueRange))) * 
					(random(phase > 2 ? maxLDelayChance : minLDelayChance) ? (phase > 2 ? delayMlMax : phase) : delayMlMin));
			if (second && isDouble)
				delay = randomInRange(prev - 3, prev + 3);
			second = !second;
			ret.add(delay);
			prev = delay;
		}
		if (powered)
		{
			int j;
			float power;
			for (int i = 0; i < ret.size(); i++)
			{
				j = Math.round((randomInRange(3, 7) * 10f) / (phase > 5 ? 5 : phase));
				power = randomInRange(j - (j / 20), j + (j / 20)) / 100;
				ret.set(i, Math.round(ret.get(i) * (1 + power)));
			}
		}
		return ret;
	}
	
	public static int randomInRange(int min, int max)
	{
		return min + (int)(Math.random() * ((max - min) + 1));
	}
	
	public static float randomInRange(float min, float max)
	{
		return min + (int)(Math.random() * ((max - min) + 1));
	}
	
	public static boolean random()
	{
		return randomInRange(0, 100) < 50 ? true : false;
	}
	
	public static boolean random (short chanse)
	{
		return randomInRange(0, 100) < chanse ? true : false;
	}
	
	public static boolean isInRange (float x, float y, float range) //return true if max - min < range
	{
		if (Math.max(x, y) - Math.min(x, y) < range)
			return true;
		return false;
	}
	
	public static boolean isInRange (double x, double y, float range)
	{
		if (Math.max(x, y) - Math.min(x, y) < range)
			return true;
		return false;
	}
	
	public static double getDistance (Vec3d pos1, Vec3d pos2)
	{
		double dX = Math.max(pos1.x, pos2.x) - Math.min(pos1.x, pos2.x);  
        double dY = Math.max(pos1.y, pos2.y) - Math.min(pos1.y, pos2.y);
        double dZ = Math.max(pos1.z, pos2.z) - Math.min(pos1.z, pos2.z);
        return (double)MathHelper.sqrt(dX * dX + dY * dY + dZ * dZ);
	}
	
	public static Vec3d getCenter (Entity entity)
	{
		double x = entity.posX + entity.width / 2;
		double y = entity.posY + entity.height / 2;
		double z = entity.posZ + entity.width / 2;
		return new Vec3d(x, y, z);
	}
	
	public static void sleep (long ms)
	{
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void sleep (long ms, int ns)
	{
		try {
			Thread.sleep(ms, ns);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static Direction getDirection (boolean two, float yaw) //two если возвращать промежуточное направление (southwest напр.)
	{
		Direction ret = null;
		if (yaw >= 46 && yaw <= 135)
			ret = Direction.Xneg;
		if ((yaw >= 136 && yaw <= 180) || yaw <= -136)
			ret = Direction.Zneg;
		if ((yaw <= 45 && yaw <= 0) || (yaw >= -45 && yaw <= 0))
			ret = Direction.Zpos;
		if (ret == null)
			ret = Direction.Xpos;
		if (two)
		{
			if (yaw > 30 && yaw < 60)
				ret = Direction.XnegZpos;
			if (yaw > 120 && yaw < 150)
				ret = Direction.XnegZneg;
			if (yaw > -60 && yaw < -30)
				ret = Direction.XposZneg;
			if (yaw > -150 && yaw < -120)
				ret = Direction.XposZpos;
		}	
		return ret;
	}
	
	public static Direction getDirection (boolean two)
	{
		return getDirection(two, MC.player.rotationYaw);
	}
	
	public static enum Direction
	{
		Xpos,
		Xneg,
		Zpos,
		Zneg,
		XposZpos,
		XposZneg,
		XnegZpos,
		XnegZneg;
	}
	
	public static boolean canAttackEntity()
	{
		return MC.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY;
	}
	
	public static boolean canAttackEntity (Entity entity)
	{
		return MC.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY && MC.objectMouseOver.entityHit.equals(entity);
	}
	
	@Deprecated
	public static boolean rayTrace(Vec3d start, Vec3d end, boolean calcEntities)
    {
		Vec3d min = new Vec3d (Math.min(start.x, end.x), Math.min(start.y, end.y), Math.min(start.z, end.z));
		Vec3d max = new Vec3d (Math.max(start.x, end.x), Math.max(start.y, end.y), Math.max(start.z, end.z));
		double x = min.x, y = min.y, z = min.z;
		Vec3d delta = new Vec3d(
				max.x - min.x,
				max.y - min.y, 
				max.z - min.z);
		double dist = getDistance(start, end);
		Vec3d step = new Vec3d(delta.x / (dist * 8), delta.y / (dist * 8), delta.z / (dist * 8));
		IBlockState block;
		while (true)
		{
			if (x < max.x)
				x += step.x;
			if (y < max.y)
				y += step.y;
			if (z < max.z)
				z += step.z;
			block = MC.world.getBlockState(new BlockPos(x, y, z));
			if (calcEntities)
			{
				for (Entity e : MC.world.loadedEntityList)
				{
					if (e.getCollisionBoundingBox().contains(new Vec3d(x, y, z)))
						return false;
				}
			}
			AxisAlignedBB aabb = block.getCollisionBoundingBox(MC.world, new BlockPos(x, y, z));
			if (MC.world != null && block.getBlock().canCollideCheck(block, false) && aabb.contains(new Vec3d(x, y, z)))
				return false;
			if (x >= max.x && y >= max.y && z >= max.z)
				break;
		}
		return true;
    }
	
	public static boolean rayTraceBlocks (Vec3d start, Vec3d end)
	{
		RayTraceResult result = MC.world.rayTraceBlocks(start, end);
		return result == null || result.typeOfHit != RayTraceResult.Type.BLOCK;
	}
	
	@Deprecated
	public static boolean rayTrace (Vec3d start, Vec3d end)
	{
		return rayTrace(start, end, false);
	}
	
	public static boolean isNearest (Entity entity1, Entity entity2)
	{
		return MC.player.getDistanceToEntity(entity1) < MC.player.getDistanceToEntity(entity2);
	}
	
	public static boolean isSmallerBB (Entity entity1, Entity entity2, Direction dir)
	{
		AxisAlignedBB aabb1 = entity1.getEntityBoundingBox();
		AxisAlignedBB aabb2 = entity2.getEntityBoundingBox();
		boolean x = (aabb1.maxX - aabb1.minX < aabb2.maxX - aabb2.minX);
		boolean z = (aabb1.maxZ - aabb1.minZ < aabb2.maxZ - aabb2.minZ);
		return ((dir == Direction.Xneg || dir == Direction.Xpos) && x) || ((dir == Direction.Zneg || dir == Direction.Zpos) && z);
	}

	public static class EntityLookHelper
	{
	    private final EntityPlayerSP entity;
	    /** The amount of change that is made each update for an entity facing a direction. */
	    private float deltaLookYaw;
	    /** The amount of change that is made each update for an entity facing a direction. */
	    private float deltaLookPitch;
	    /** Whether or not the entity is trying to look at something. */
	    private boolean isLooking;
	    private double posX;
	    private double posY;
	    private double posZ;

	    public EntityLookHelper(EntityPlayerSP entitylivingIn)
	    {
	        this.entity = entitylivingIn;
	    }

	    /**
	     * Sets position to look at using entity
	     */
	    public void setLookPositionWithEntity(Entity entityIn, float deltaYaw, float deltaPitch)
	    {
	        this.posX = entityIn.posX;
	        this.posY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0D;

	        this.posZ = entityIn.posZ;
	        this.deltaLookYaw = deltaYaw;
	        this.deltaLookPitch = deltaPitch;
	        this.isLooking = true;
	    }

	    /**
	     * Sets position to look at
	     */
	    public void setLookPosition(double x, double y, double z, float deltaYaw, float deltaPitch)
	    {
	        this.posX = x;
	        this.posY = y;
	        this.posZ = z;
	        this.deltaLookYaw = deltaYaw;
	        this.deltaLookPitch = deltaPitch;
	        this.isLooking = true;
	    }

	    /**
	     * Updates look
	     */
	    public void onUpdateLook (float dPitch, boolean onlyServer)
	    {
	    	float yaw = 0;
	    	float pitch = 0;
	    	float yawHead = 0;
	    	
	        if (this.isLooking)
	        {
	            this.isLooking = false;
	            double d0 = this.posX - this.entity.posX;
	            double d1 = this.posY - (this.entity.posY + (double)this.entity.getEyeHeight());
	            double d2 = this.posZ - this.entity.posZ;
	            double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
	            float f = (float)(MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
	            float f1 = (float)(-(MathHelper.atan2(d1, d3) * (180D / Math.PI)));
	            pitch = this.updateRotation(this.entity.rotationPitch, f1, this.deltaLookPitch);
	            yaw = this.updateRotation(this.entity.rotationYaw, f, this.deltaLookYaw);
	        }
	        else
	        {
	            yawHead = this.updateRotation(this.entity.rotationYawHead, this.entity.renderYawOffset, 10.0F);
	        }

	        float f2 = MathHelper.wrapDegrees(this.entity.rotationYawHead - this.entity.renderYawOffset);
	        
	        if (f2 < -75.0F)
	        {
	        	yawHead = this.entity.renderYawOffset - 75.0F;
	        }

	        if (f2 > 75.0F)
	        {
	        	yawHead = this.entity.renderYawOffset + 75.0F;
	        }
	        if (onlyServer)
	        {
	        	Baritone.yaw = yaw;
	        	Baritone.pitch = pitch;
	        }
	        if (!onlyServer)
	        {	
	        	this.entity.rotationYaw = yaw;
	        	this.entity.rotationPitch = pitch;
	        }
	        this.entity.rotationYawHead = yawHead;
	    }
	    
	    public void onUpdateLook (float dPitch)
	    {
	    	this.onUpdateLook(dPitch, false);
	    }

	    private float updateRotation(float p_75652_1_, float p_75652_2_, float p_75652_3_)
	    {
	        float f = MathHelper.wrapDegrees(p_75652_2_ - p_75652_1_);

	        if (f > p_75652_3_)
	        {
	            f = p_75652_3_;
	        }

	        if (f < -p_75652_3_)
	        {
	            f = -p_75652_3_;
	        }

	        return p_75652_1_ + f;
	    }

	    public boolean getIsLooking()
	    {
	        return this.isLooking;
	    }

	    public double getLookPosX()
	    {
	        return this.posX;
	    }

	    public double getLookPosY()
	    {
	        return this.posY;
	    }

	    public double getLookPosZ()
	    {
	        return this.posZ;
	    }
	}
}
