import java.util.concurrent.Callable;

public class Controller {
    public Controller(Config config) {
        config.setController(this);

        Data data = new Data(config);
        Cat cat = new Cat(data);

        config.setData(data);
        config.setCat(cat);

        UI ui = new UI(config);
        config.setUI(ui);
    }
}
