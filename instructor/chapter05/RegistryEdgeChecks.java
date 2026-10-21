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
