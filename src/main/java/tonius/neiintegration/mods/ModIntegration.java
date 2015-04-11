package tonius.neiintegration.mods;

import java.util.List;

import tonius.neiintegration.IntegrationBase;
import tonius.neiintegration.mods.bigreactors.BigReactorsIntegration;
import tonius.neiintegration.mods.electricalage.ElectricalAgeIntegration;
import tonius.neiintegration.mods.forestry.ForestryIntegration;
import tonius.neiintegration.mods.harvestcraft.HarvestCraftIntegration;
import tonius.neiintegration.mods.mcforge.MCForgeIntegration;
import tonius.neiintegration.mods.minefactoryreloaded.MFRIntegration;
import tonius.neiintegration.mods.railcraft.RailcraftIntegration;

public abstract class ModIntegration {
    
    public static void init(List<IntegrationBase> integrations) {
        integrations.add(new MCForgeIntegration());
        integrations.add(new BigReactorsIntegration());
        integrations.add(new ElectricalAgeIntegration());
        integrations.add(new ForestryIntegration());
        integrations.add(new HarvestCraftIntegration());
        integrations.add(new MFRIntegration());
        integrations.add(new RailcraftIntegration());
    }
    
}
