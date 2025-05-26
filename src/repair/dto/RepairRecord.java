package repair.dto;

public class RepairRecord {
    private String partName;
    private String maintenanceDate;
    private String maintenanceDuration;
    private String employeeId;

    public RepairRecord(String partName, String maintenanceDate, String maintenanceDuration, String employeeId) {
        this.partName = partName;
        this.maintenanceDate = maintenanceDate;
        this.maintenanceDuration = maintenanceDuration;
        this.employeeId = employeeId;
    }

    public String getPartName() {
        return partName;
    }

    public String getMaintenanceDate() {
        return maintenanceDate;
    }

    public String getMaintenanceDuration() {
        return maintenanceDuration;
    }

    public String getEmployeeId() {
        return employeeId;
    }
}
