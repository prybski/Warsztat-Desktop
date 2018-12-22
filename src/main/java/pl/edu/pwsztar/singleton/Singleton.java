package pl.edu.pwsztar.singleton;

import pl.edu.pwsztar.model.repository.*;

public class Singleton {

    private ClientRepository clientRepository;
    private VehicleRepository vehicleRepository;
    private JobRepository jobRepository;
    private TaskRepository taskRepository;
    private DemandRepository demandRepository;
    private PartRepository partRepository;

    {
        clientRepository = new ClientRepository();
        vehicleRepository = new VehicleRepository();
        jobRepository = new JobRepository();
        taskRepository = new TaskRepository();
        demandRepository = new DemandRepository();
        partRepository = new PartRepository();
    }

    private Singleton() {
    }

    public static Singleton getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public ClientRepository getClientRepository() {
        return clientRepository;
    }

    public VehicleRepository getVehicleRepository() {
        return vehicleRepository;
    }

    public JobRepository getJobRepository() {
        return jobRepository;
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }

    public DemandRepository getDemandRepository() {
        return demandRepository;
    }

    public PartRepository getPartRepository() {
        return partRepository;
    }

    private static class SingletonHelper {
        private static final Singleton INSTANCE;

        static {
            INSTANCE = new Singleton();
        }
    }
}
