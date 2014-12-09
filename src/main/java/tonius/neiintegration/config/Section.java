package tonius.neiintegration.config;

public class Section {
    
    public String name;
    public String id;
    
    public Section(String name, String id) {
        this.name = name;
        this.id = id;
        Config.configSections.add(this);
    }
    
    public String toLowerCase() {
        return this.name.toLowerCase();
    }
    
}
