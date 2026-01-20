package com.pianistwb.merged_magic.events;

import com.pianistwb.merged_magic.MergedMagic;
import com.pianistwb.merged_magic.networking.ParticlePayload;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = MergedMagic.MODID)
public class ModEvents {
	@SubscribeEvent
	public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar("1");
		registrar.playToClient(ParticlePayload.TYPE, ParticlePayload.STREAM_CODEC, ParticlePayload::handleDataOnClient);
	}
}
