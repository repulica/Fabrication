package com.unascribed.fabrication;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.Unit;
import net.minecraft.util.profiler.Profiler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.unascribed.fabrication.interfaces.GetServerConfig;
import com.unascribed.fabrication.logic.WoinaDrops;
import com.unascribed.fabrication.support.MixinConfigPlugin;

public class FabricationModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		FabricationClientCommands.registerCommands(ClientCommandManager.DISPATCHER);

		if (!MixinConfigPlugin.isBanned("*.classic_block_drops")) {
			MinecraftClient.getInstance().send(() -> {
				((ReloadableResourceManager)MinecraftClient.getInstance().getResourceManager()).registerReloader(new ResourceReloader() {
					@Override
					public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
						return synchronizer.whenPrepared(Unit.INSTANCE).thenRunAsync(() -> {
							WoinaDrops.mippedBlocksInvalid = true;
						}, applyExecutor);
					}

				});
			});
		}
	}

	public static boolean isBannedByServer(String configKey) {
		if (MinecraftClient.getInstance() == null) return false;
		ClientPlayNetworkHandler net = MinecraftClient.getInstance().getNetworkHandler();
		if (net != null && net instanceof GetServerConfig) {
			return ((GetServerConfig)net).fabrication$getServerBanned().contains(configKey);
		}
		return false;
	}
}
