package com.ngames.nclient.mixin;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ngames.nclient.module.Modules;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.client.CPacketPlayer.PositionRotation;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketVehicleMove;

@Mixin(NetworkManager.class)
public class MixinNetworkManager
{
	@Inject(method = "sendPacket(Lnet/minecraft/network/Packet;Lio/netty/util/concurrent/GenericFutureListener;[Lio/netty/util/concurrent/GenericFutureListener;)V", at = @At("HEAD"), cancellable = true)
	private void sendPacket(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> p_sendPacket_2_, GenericFutureListener<? extends Future<? super Void>>[] p_sendPacket_3_, CallbackInfo info)
	{
		if (Modules.packetCanceller.isEnabled())
		{
			if (packet instanceof CPacketInput && Modules.packetCanceller.CPacketInput.getValue())
				info.cancel();
			if (packet instanceof Position && Modules.packetCanceller.CPacketPosition.getValue())
				info.cancel();
			if (packet instanceof PositionRotation && Modules.packetCanceller.CPacketPositionRotation.getValue())
				info.cancel();
			if (packet instanceof Rotation && Modules.packetCanceller.CPacketRotation.getValue())
				info.cancel();
			if (packet instanceof CPacketPlayerAbilities && Modules.packetCanceller.CPacketPlayerAbilities.getValue())
				info.cancel();
			if (packet instanceof CPacketPlayerDigging && Modules.packetCanceller.CPacketPlayerDigging.getValue())
				info.cancel();
			if (packet instanceof CPacketPlayerTryUseItem && Modules.packetCanceller.CPacketPlayerTryUseItem.getValue())
				info.cancel();
			if (packet instanceof CPacketPlayerTryUseItemOnBlock && Modules.packetCanceller.CPacketPlayerTryUseItemOnBlock.getValue())
				info.cancel();
			if (packet instanceof CPacketEntityAction && Modules.packetCanceller.CPacketEntityAction.getValue())
				info.cancel();
			if (packet instanceof CPacketUseEntity && Modules.packetCanceller.CPacketUseEntity.getValue())
				info.cancel();
			if (packet instanceof CPacketVehicleMove && Modules.packetCanceller.CPacketVehicleMove.getValue())
				info.cancel();
			if (info.isCancelled())
				Modules.packetCanceller.inHud.set(((int) Modules.packetCanceller.inHud.get())+1);
		}
	}

	@Inject(method = "channelRead0", at = @At("HEAD"))
	private void receivePacket(ChannelHandlerContext p_channelRead0_1_, Packet<?> p_channelRead0_2_, CallbackInfo ci) {

	}
}
