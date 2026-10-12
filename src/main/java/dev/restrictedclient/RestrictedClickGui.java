package dev.restrictedclient;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class RestrictedClickGui extends Screen {
    private String category = "Visuals";
    private int px, py, pw = 430, ph = 280;
    private static final int PURPLE = 0xFFB98AFF, GLASS = 0xE8171024, WHITE = 0xFFF1E8FF;
    public RestrictedClickGui() { super(Text.literal("Restricted Client")); }

    @Override protected void init() {
        px = (width - pw) / 2; py = (height - ph) / 2;
        buildButtons();
    }
    private void buildButtons() {
        clearChildren();
        String[] categories = {"Combat", "Visuals", "Movement", "Player", "Misc"};
        for (int i = 0; i < categories.length; i++) {
            String c = categories[i];
            addDrawableChild(ButtonWidget.builder(Text.literal(c), b -> { category = c; buildButtons(); })
                .dimensions(px + 12, py + 49 + i * 35, 108, 28).build());
        }
        String[][] rows = switch (category) {
            case "Visuals" -> new String[][]{{"HUD", "hud"}, {"ESP", "esp"}, {"Storage ESP", "storage"}, {"Keystrokes", "keys"}, {"CPS Counter", "cps"}, {"Armor HUD", "armor"}, {"Animations", "animations"}};
            case "Movement" -> new String[][]{{"Sprint", "sprint"}};
            case "Player" -> new String[][]{{"Armor HUD", "armor"}, {"HUD", "hud"}};
            case "Combat" -> new String[][]{{"Practice utilities", "practice"}};
            default -> new String[][]{{"ClickGUI key: Right Shift", "info"}};
        };
        for (int i = 0; i < rows.length; i++) {
            String name = rows[i][0], id = rows[i][1];
            addDrawableChild(ButtonWidget.builder(Text.literal(name + "  [" + state(id) + "]"), b -> { toggle(id); buildButtons(); })
                .dimensions(px + 139 + (i % 2) * 136, py + 51 + (i / 2) * 43, 130, 31).build());
        }
        addDrawableChild(ButtonWidget.builder(Text.literal("Done"), b -> close())
            .dimensions(px + pw - 85, py + ph - 35, 72, 24).build());
    }
    private String state(String id) {
        return switch (id) {
            case "hud" -> on(RestrictedClient.hud); case "esp" -> on(RestrictedClient.esp);
            case "storage" -> on(RestrictedClient.storageEsp); case "keys" -> on(RestrictedClient.keystrokes);
            case "cps" -> on(RestrictedClient.cpsCounter); case "armor" -> on(RestrictedClient.armorHud);
            case "animations" -> on(RestrictedClient.animations); case "sprint" -> on(RestrictedClient.sprint);
            case "practice" -> "READY"; default -> "INFO";
        };
    }
    private String on(boolean v) { return v ? "ON" : "OFF"; }
    private void toggle(String id) {
        switch (id) {
            case "hud" -> RestrictedClient.hud = !RestrictedClient.hud;
            case "esp" -> RestrictedClient.esp = !RestrictedClient.esp;
            case "storage" -> RestrictedClient.storageEsp = !RestrictedClient.storageEsp;
            case "keys" -> RestrictedClient.keystrokes = !RestrictedClient.keystrokes;
            case "cps" -> RestrictedClient.cpsCounter = !RestrictedClient.cpsCounter;
            case "armor" -> RestrictedClient.armorHud = !RestrictedClient.armorHud;
            case "animations" -> RestrictedClient.animations = !RestrictedClient.animations;
            case "sprint" -> RestrictedClient.sprint = !RestrictedClient.sprint;
        }
    }
    @Override public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        renderBackground(ctx, mouseX, mouseY, delta);
        RestrictedClient.panel(ctx, px - 5, py - 5, px + pw + 5, py + ph + 5, 0x702B1550, 0xFF8E55D9);
        RestrictedClient.panel(ctx, px, py, px + pw, py + ph, GLASS, PURPLE);
        ctx.fill(px + 1, py + 1, px + pw - 1, py + 36, 0xF02B1A40);
        ctx.drawTextWithShadow(textRenderer, Text.literal("RESTRICTED CLIENT"), px + 14, py + 10, WHITE);
        ctx.drawTextWithShadow(textRenderer, Text.literal("PURPLE GLASS  /  " + category.toUpperCase()), px + 14, py + 24, 0xFFC9A9F4);
        ctx.fill(px + 128, py + 44, px + 129, py + ph - 12, 0xFF70439B);
        super.render(ctx, mouseX, mouseY, delta);
    }
    @Override public boolean shouldPause() { return false; }
}
