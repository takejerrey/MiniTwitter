package visitor;

public interface Node {

    public void accept(visitor.NodeVisitor visitor);

}