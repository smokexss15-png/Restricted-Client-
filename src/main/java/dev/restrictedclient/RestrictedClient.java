package dev.restrictedclient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class RestrictedClient implements ClientModInitializer {
    public static final String MOD_ID = "restrictedclient";
    public static boolean hud = true, esp = false, storageEsp = false;
    public static boolean keystrokes = true, cpsCounter = true, animations = true;
    public static boolean sprint = false, armorHud = true;
    public static int clicks, cps;
    private static long cpsWindow = System.currentTimeMillis();
    private static KeyBinding openGui;

    @Override public void onInitializeClient() {
        openGui = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.restrictedclient.clickgui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT,
            KeyBinding.Category.create(Identifier.of(MOD_ID, "main"))));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGui.wasPressed()) {
                if (client.currentScreen instanceof RestrictedClickGui) client.setScreen(null);
                else if (client.currentScreen == null) client.setScreen(new RestrictedClickGui());
            }
            long now = System.currentTimeMillis();
            if (now - cpsWindow >= 1000) { cps = clicks; clicks = 0; cpsWindow = now; }
            if (sprint && client.player != null && !client.player.isSneaking())
                client.player.setSprinting(client.player.input.movementForward > 0);
        });
        HudRenderCallback.EVENT.register((ctx, tickCounter) -> renderHud(ctx));
    }

    // Can be called by a future mouse-input hook; CPS currently displays the count received here.
    public static void registerClick() { clicks++; }

    private static void renderHud(DrawContext ctx) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.options.hudHidden) return;
        if (hud) {
            panel(ctx, 5, 5, 156, 33, 0xD51A1028, 0xFFB98AFF);
            ctx.drawTextWithShadow(mc.textRenderer, Text.literal("RESTRICTED CLIENT"), 11, 10, 0xFFDCCBFF);
            ctx.drawTextWithShadow(mc.textRenderer, Text.literal("1.21.11  |  Practice"), 11, 21, 0xFFBCA8D9);
        }
        if (cpsCounter) {
            int w = mc.getWindow().getScaledWidth();
            panel(ctx, w - 78, 6, w - 6, 28, 0xD51A1028, 0xFFB98AFF);
            ctx.drawTextWithShadow(mc.textRenderer, Text.literal("CPS: " + cps), w - 68, 13, 0xFFDCCBFF);
        }
        if (keystrokes) {
            int x = 10, y = mc.getWindow().getScaledHeight() - 66;
            key(ctx, x + 22, y, "W", mc.options.forwardKey.isPressed());
            key(ctx, x, y + 22, "A", mc.options.leftKey.isPressed());
            key(ctx, x + 22, y + 22, "S", mc.options.backKey.isPressed());
            key(ctx, x + 44, y + 22, "D", mc.options.rightKey.isPressed());
            key(ctx, x + 70, y + 22, "J", mc.options.jumpKey.isPressed());
        }
        if (armorHud) {
            int x = mc.getWindow().getScaledWidth() / 2 + 96;
            int y = mc.getWindow().getScaledHeight() - 25;
            EquipmentSlot[] slots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
            for (int i = 0; i < slots.length; i++) {
                ItemStack stack = mc.player.getEquippedStack(slots[i]);
                ctx.drawItem(stack, x + i * 18, y);
                if (!stack.isEmpty() && stack.isDamageable()) {
                    int remaining = stack.getMaxDamage() - stack.getDamage();
                    ctx.drawTextWithShadow(mc.textRenderer, Text.literal(Integer.toString(remaining)), x + i * 18, y - 9, 0xFFDCCBFF);
                }
            }
        }
    }

    static void panel(DrawContext ctx, int x1, int y1, int x2, int y2, int fill, int border) {
        ctx.fill(x1, y1, x2, y2, fill);
        ctx.fill(x1, y1, x2, y1 + 1, border); ctx.fill(x1, y2 - 1, x2, y2, border);
        ctx.fill(x1, y1, x1 + 1, y2, border); ctx.fill(x2 - 1, y1, x2, y2, border);
    }
    private static void key(DrawContext ctx, int x, int y, String label, boolean down) {
        panel(ctx, x, y, x + 20, y + 20, down ? 0xFF7444A8 : 0xD51A1028, 0xFFB98AFF);
        ctx.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, Text.literal(label), x + 6, y + 6, 0xFFFFFFFF);
    }
}
