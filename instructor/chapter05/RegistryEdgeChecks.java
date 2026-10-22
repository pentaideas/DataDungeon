/*
 * Data Dungeon: The Linked Labyrinth, companion to Pat Morin's
 * Open Data Structures (Java): https://opendatastructures.org/ods-java/
 * Morin material: https://creativecommons.org/licenses/by/2.5/ca/
 * Original project contributions: https://creativecommons.org/publicdomain/zero/1.0/
 * Changes include dungeon examples, simplified contracts and teaching exercises.
 * Preserve upstream notices with adaptations. No endorsement is implied.
 * Chapter sources and changes: https://github.com/pentaideas/DataDungeon/blob/main/SOURCES.md
 */

public class RegistryEdgeChecks {
    private static void check(boolean ok, String label) {
        if (!ok) throw new AssertionError(label);
    }

    public static void main(String[] args) {
        ArtifactRegistry registry = ArtifactRegistry.sample();
        check("well".equals(registry.get("Aa")), "tail lookup");
        check("archive".equals(registry.get(new String("C"))),
            "equal text in different objects");
        check("forge".equals(registry.remove("BB")), "middle removal");
        check("archive".equals(registry.get("C"))
            && "well".equals(registry.get("Aa"))
            && registry.size() == 2, "survivors after removal");
        System.out.println("Registry edge checks passed");
    }
}
