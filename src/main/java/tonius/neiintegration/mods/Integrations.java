package tonius.neiintegration.mods;

import java.util.ArrayList;
import java.util.List;

import tonius.neiintegration.IntegrationBase;
import tonius.neiintegration.mods.bigreactors.BigReactorsIntegration;
import tonius.neiintegration.mods.electricalage.ElectricalAgeIntegration;
import tonius.neiintegration.mods.forestry36.Forestry36Integration;
import tonius.neiintegration.mods.harvestcraft.HarvestCraftIntegration;
import tonius.neiintegration.mods.mcforge.MCForgeIntegration;
import tonius.neiintegration.mods.minefactoryreloaded.MFRIntegration;
import tonius.neiintegration.mods.railcraft.RailcraftIntegration;

public class Integrations {
    
    public static List<IntegrationBase> getIntegrations() {
        List<IntegrationBase> i = new ArrayList<IntegrationBase>();
        
        i.add(new MCForgeIntegration());
        i.add(new BigReactorsIntegration());
        i.add(new ElectricalAgeIntegration());
        i.add(new Forestry36Integration());
        i.add(new HarvestCraftIntegration());
        i.add(new MFRIntegration());
        i.add(new RailcraftIntegration());
        
        return i;
    }
    
}
