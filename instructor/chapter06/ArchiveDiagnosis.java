public class ArchiveDiagnosis {
    public static void main(String[] args) {
        DoorArchive archive = DoorArchive.sample();
        check("archive".equals(archive.get(40)), "root lookup");
        check("well".equals(archive.get(20)), "left child lookup");
        check("forge".equals(archive.get(60)), "right child lookup");
        check(archive.get(25) == null, "missing door");
        check(archive.size() == 7, "lookup preserves size");
        System.out.println("Archive diagnosis passed");
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
