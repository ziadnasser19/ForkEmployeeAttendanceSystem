package Backend.AttendanceManagment;

import Backend.AttendanceManagment.AttendanceStates.IAttendanceState;
import Backend.AttendanceManagment.AttendanceStates.InState;
import Backend.AttendanceManagment.AttendanceStates.OutState;
import Utilities.DataHandling.AttendanceFileHandler;
import Utilities.DataHandling.IAttendanceDataHandler;
import DTO.DTO;

import java.text.SimpleDateFormat;
import java.util.List;

public class AttendanceManager implements IAttendanceManager {

    private int employeeID;
    private List<AttendanceRecord> attendanceRecords;
    private IAttendanceState attendanceState;

    public void setAttendanceState(IAttendanceState attendanceState) {
        this.attendanceState = attendanceState;
    }

    /**
     * The constructor uses the ID to get the read the records
     * and checks if the state in or out.
     * @param ID
     */
    public AttendanceManager(int ID) {
        employeeID = ID;
        attendanceRecords = new AttendanceFileHandler().getRecords(ID);
        if (attendanceRecords.size() != 0){
            if (attendanceRecords.get(attendanceRecords.size() - 1).getTimeOut() == null){
                attendanceState = new InState();
            }
        }else{
            attendanceState = new OutState();
        }
    }


    public void markAttendance() {
        attendanceState.markAttendance(attendanceRecords, this);
        new AttendanceFileHandler().editRecord(employeeID, attendanceRecords);
    }

    public void editAttendance(int ID, AttendanceRecord updatedRecord) {
        AttendanceFileHandler fileHandler = new AttendanceFileHandler();
        List<AttendanceRecord> updatedAttendanceRecords = fileHandler.getRecords(ID);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String updatedDate = formatter.format(updatedRecord.getDate());
        String existedDate;
        for (int i = 0; i < updatedAttendanceRecords.size(); i++) {
            existedDate = formatter.format(updatedAttendanceRecords.get(i).getDate());
            if (updatedDate.equals(existedDate)) {
                updatedAttendanceRecords.set(i, updatedRecord);
                break;
            }
        }
        fileHandler.editRecord(ID, updatedAttendanceRecords);
    }

    public void viewAttendanceHistory(DTO records) {
        superViewAttendanceHistory(employeeID, records);
    }

    public void superViewAttendanceHistory(int ID, DTO records) {
        IAttendanceDataHandler attendanceDataHandler = new AttendanceFileHandler();
        attendanceRecords = attendanceDataHandler.getRecords(ID);

        records.setAttendanceRecords(attendanceRecords);
    }
}
