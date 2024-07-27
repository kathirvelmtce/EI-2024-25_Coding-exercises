// Product
class Computer {
    private String cpu;
    private String ram;
    private String storage;
    private String gpu;
    private String operatingSystem;

    public void setCpu(String cpu) { this.cpu = cpu; }
    public void setRam(String ram) { this.ram = ram; }
    public void setStorage(String storage) { this.storage = storage; }
    public void setGpu(String gpu) { this.gpu = gpu; }
    public void setOperatingSystem(String operatingSystem) { this.operatingSystem = operatingSystem; }

    @Override
    public String toString() {
        return "Computer{" +
                "cpu='" + cpu + '\'' +
                ", ram='" + ram + '\'' +
                ", storage='" + storage + '\'' +
                ", gpu='" + gpu + '\'' +
                ", operatingSystem='" + operatingSystem + '\'' +
                '}';
    }
}

// Builder interface
interface ComputerBuilder {
    ComputerBuilder setCpu(String cpu);
    ComputerBuilder setRam(String ram);
    ComputerBuilder setStorage(String storage);
    ComputerBuilder setGpu(String gpu);
    ComputerBuilder setOperatingSystem(String operatingSystem);
    Computer build();
}

// Concrete Builder
class StandardComputerBuilder implements ComputerBuilder {
    private Computer computer;

    public StandardComputerBuilder() {
        this.computer = new Computer();
    }

    @Override
    public ComputerBuilder setCpu(String cpu) {
        computer.setCpu(cpu);
        return this;
    }

    @Override
    public ComputerBuilder setRam(String ram) {
        computer.setRam(ram);
        return this;
    }

    @Override
    public ComputerBuilder setStorage(String storage) {
        computer.setStorage(storage);
        return this;
    }

    @Override
    public ComputerBuilder setGpu(String gpu) {
        computer.setGpu(gpu);
        return this;
    }

    @Override
    public ComputerBuilder setOperatingSystem(String operatingSystem) {
        computer.setOperatingSystem(operatingSystem);
        return this;
    }

    @Override
    public Computer build() {
        return computer;
    }
}

// Director
class ComputerAssembler {
    public Computer assembleGamingComputer(ComputerBuilder builder) {
        return builder.setCpu("Intel i9")
                      .setRam("32GB DDR4")
                      .setStorage("1TB NVMe SSD")
                      .setGpu("NVIDIA RTX 3080")
                      .setOperatingSystem("Windows 10")
                      .build();
    }

    public Computer assembleOfficeComputer(ComputerBuilder builder) {
        return builder.setCpu("Intel i5")
                      .setRam("16GB DDR4")
                      .setStorage("512GB SSD")
                      .setGpu("Integrated Graphics")
                      .setOperatingSystem("Windows 11")
                      .build();
    }
}

// Client code
public class ComputerConfigurationSystem {
    public static void main(String[] args) {
        ComputerBuilder builder = new StandardComputerBuilder();
        ComputerAssembler assembler = new ComputerAssembler();

        Computer gamingComputer = assembler.assembleGamingComputer(builder);
        System.out.println("Gaming Computer: " + gamingComputer);

        Computer officeComputer = assembler.assembleOfficeComputer(builder);
        System.out.println("Office Computer: " + officeComputer);

        // Custom configuration
        Computer customComputer = builder
                .setCpu("AMD Ryzen 7")
                .setRam("64GB DDR4")
                .setStorage("2TB NVMe SSD")
                .setGpu("AMD Radeon RX 6800")
                .setOperatingSystem("Linux")
                .build();
        System.out.println("Custom Computer: " + customComputer);
    }
}