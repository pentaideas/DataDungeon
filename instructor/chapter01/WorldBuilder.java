public class WorldBuilder {
    public static Room build() {
        Room entrance = new Room("Entrance", 3);
        Room gallery = new Room("Gallery", 2);
        Room well = new Room("Well", 2);
        Room archive = new Room("Archive", 1);

        entrance.setExit(0, gallery);
        entrance.setExit(1, well);
        gallery.setExit(0, entrance);
        gallery.setExit(1, archive);
        well.setExit(0, archive);
        archive.setExit(0, entrance);

        Room vestibule = new Room("Vestibule", 2);
        Room bridge = new Room("Bridge", 2);
        Room vault = new Room("Vault", 2);
        entrance.setExit(2, vestibule);
        vestibule.setExit(0, bridge);
        vestibule.setExit(1, vault);
        bridge.setExit(0, vault);
        vault.setExit(0, entrance);
        return entrance;
    }
}
