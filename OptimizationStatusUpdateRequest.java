/**
 * Created with IntelliJ IDEA.
 * User: japelait
 * Date: 20.8.2013
 * Time: 14:27
 * To change this template use File | Settings | File Templates.
 */
public class OptimizationStatusUpdateRequest extends BaseData{

    private int UserId;
    private int ProblemId;
    private int OptimizationId;
    private String State;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getProblemId() {
        return ProblemId;
    }

    public void setProblemId(int problemId) {
        ProblemId = problemId;
    }

    public int getOptimizationId() {
        return OptimizationId;
    }

    public void setOptimizationId(int optimizationId) {
        OptimizationId = optimizationId;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }
}
