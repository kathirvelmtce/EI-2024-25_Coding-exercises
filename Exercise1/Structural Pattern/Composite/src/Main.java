import java.util.ArrayList;
import java.util.List;

// Component
abstract class FileSystemComponent {
    protected String name;

    public FileSystemComponent(String name) {
        this.name = name;
    }

    public abstract void printStructure(String indent);
}

// Leaf
class File extends FileSystemComponent {
    public File(String name) {
        super(name);
    }

    public void printStructure(String indent) {
        System.out.println(indent + "File: " + name);
    }
}

// Composite
class Directory extends FileSystemComponent {
    private List<FileSystemComponent> children = new ArrayList<>();

    public Directory(String name) {
        super(name);
    }

    public void add(FileSystemComponent component) {
        children.add(component);
    }

    public void printStructure(String indent) {
        System.out.println(indent + "Directory: " + name);
        for (FileSystemComponent component : children) {
            component.printStructure(indent + "  ");
        }
    }
}

// Usage
public class Main {
    public static void main(String[] args) {
        Directory root = new Directory("root");
        Directory home = new Directory("home");
        Directory documents = new Directory("documents");
        File file1 = new File("file1.txt");
        File file2 = new File("file2.txt");

        root.add(home);
        home.add(documents);
        documents.add(file1);
        documents.add(file2);

        root.printStructure("");
    }
}