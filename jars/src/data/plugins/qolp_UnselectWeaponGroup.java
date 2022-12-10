package data.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.input.InputEventAPI;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class qolp_UnselectWeaponGroup extends BaseEveryFrameCombatPlugin {

    public static final String ID = "qolp_UnselectWeaponGroup";
    public static final String SETTINGS_PATH = "QoLPack.ini";

    private CombatEngineAPI engine;

    @Override
    public void init(CombatEngineAPI engine) {
        this.engine = engine;

        boolean enable = true;
        try {
            JSONObject cfg = Global.getSettings().getMergedJSONForMod(SETTINGS_PATH, ID);
            enable = cfg.getBoolean("UnselectWeaponGroupOnCombatStart");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        if (!enable) {
            engine.removePlugin(this);
        }
    }

    @Override
    public void advance(float amount, List<InputEventAPI> events) {
        if (engine == null || engine.getPlayerShip() == null) return;
        ShipAPI player = engine.getPlayerShip();
        if (player == null) return;
        // If this key is set, then we've already unselected the main weapon group this combat.
        if (player.getCustomData().containsKey(ID)) return;
        // Set the custom data key
        player.setCustomData(ID, true);
        // Select an invalid group, which makes all weapon groups not selected.
        player.giveCommand(ShipCommand.SELECT_GROUP, null, -1);
    }
}
