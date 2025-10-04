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

    public static TaskStatus fromDbValue(String dbValue) {
        for (TaskStatus value : TaskStatus.values()) {
            if (value.dbValue.equals(dbValue)){
                return value;
            }
        }
        throw new IllegalArgumentException("Неіснуючий статус: " + dbValue);
    }
}
