package models;

public class Department {
    private int idep;
    private String local;
    private String chef_dep;
    private int code;

    public Department() {}

    public Department(String local, String chef_dep, int code) {
        this.local = local;
        this.chef_dep = chef_dep;
        this.code = code;
    }



    public int getIdep() {
        return idep;
    }

    public void setIdep(int idep) {
        this.idep = idep;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getChef_dep() {
        return chef_dep;
    }

    public void setChef_dep(String chef_dep) {
        this.chef_dep = chef_dep;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Department(int idep, String local, String chef_dep, int code) {
        this.idep = idep;
        this.local = local;
        this.chef_dep = chef_dep;
        this.code = code;
    }

    @Override
    public String toString() {
        return "department{" +
                "idep=" + idep +
                ", local='" + local + '\'' +
                ", chef_dep='" + chef_dep + '\'' +
                ", code=" + code +
                '}';
    }

}
