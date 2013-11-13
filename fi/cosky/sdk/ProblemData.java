package fi.cosky.sdk;
import java.util.Date;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

public class ProblemData extends BaseData {
    private int Id;
    private Date CreationDate;
    private Date ModifiedDate;
    private String Name;

    public ProblemData(String name) {
        this.Name = name;
    }

    public static ProblemData createSolutionDto() {
        return new ProblemData("exampleSolution");
    }

    public void setId(int id) {
        this.Id = id;
    }

    public void setCreationDate(Date date) {
        this.CreationDate = date;
    }

    public void setModifiedDate(Date date) {
        this.ModifiedDate = date;
    }



    public void setName(String name) {
        this.Name = name;
    }
    public int getId() {
        return Id;
    }

    public Date getCreationDate() {
        return CreationDate;
    }

    public Date getModifiedDate() {
        return ModifiedDate;
    }

    @Override
    public String toString() {
       /* return "ProblemData{" +
                "Id=" + Id +
                ", CreationDate=" + CreationDate +
                ", ModifiedDate=" + ModifiedDate +
                ", Name='" + Name + '\'' +
                '}';*/
        return API.gson.toJson(this);
    }

    public String getName() {
        return Name;
    }


}
