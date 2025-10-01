package skillclan.taskmanager.model;

public enum TaskStatus {
    NEW ("NEW"),
    IN_PROGRESS ("IN PROGRESS"),
    DONE ("DONE");

    private final String dbValue;

    TaskStatus(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }
}
