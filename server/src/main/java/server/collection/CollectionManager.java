package server.collection;

import interaction.Response;
import server.io.ClientComManager;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Класс управляющий коллекцией
 * @author railolog
 * @version 1.0
 */
public interface CollectionManager<T> {
    /**
     * Функция получения уникального id
     * @return Возвращает id
     */
    long createUniqueId();

    void setCommunicationManager(ClientComManager communicationManager);

    /**
     * Устанавливает коллекцией, передаваемое ей значение
     * @param collection Коллекция для устаовки
     */
    void setCollection(ArrayList<T> collection);

    /**
     * Выводит информацию о коллекции
     * @return
     */
    Response showCollectionInfo();

    /**
     * Ввыодит содержимое коллекции
     */
    Response printElements();

    /**
     * Запускает процесс добавление элемента в коллекцию
     * @return
     */
    Response addElement() throws IOException, ClassNotFoundException;

    /**
     * Обновляет значение элемента по id
     * @param id id элемента, который необходимо обновить
     */
    Response update(long id) throws IOException, ClassNotFoundException;

    /**
     * Обновляет значение элемента по id
     * @param id id элемента, который необходимо обновить
     */
    Response update(String id) throws IOException, ClassNotFoundException;

    /**
     * Возвращает индекс элемента в коллекции
     * @param id id элемента
     * @return Возвращает индекс
     */
    int getIndexById(long id);

    /**
     * Удаляет элемент коллекции
     * @param id id удаляемого элемента
     */
    Response removeById(String id);

    /**
     * Очистка коллекции
     */
    Response clearCollection();

    /**
     * Добавление элемента в коллекцию, если он больше максимального
     * существующего элемента
     * @return
     */
    Response addIfMax() throws IOException, ClassNotFoundException;

    /**
     * Перемешка коллекции
     */
    Response shuffle();

    /**
     * Упорядычивает коллекцию в обратном порядке
     * @return
     */
    Response reorder();

    /**
     * Возвращает минимальный по полю creationDate
     * элемент
     * @see app.core.City#creationDate
     */
    Response minByCreationDate();

    /**
     * Выводит элементы, значение поля которых, превышает данное
     * @param metersAboveSeaLevel сравниваемое значение
     * @see app.core.City#metersAboveSeaLevel
     * @return
     */
    Response filterGreaterThanSeaLevel(String metersAboveSeaLevel);

    /**
     * Сортирует коллекцию
     */
    void sort();

    /**
     * Сохраняет коллекцию по заданному пути
     * @param path путь до файла
     */
    void save(String path);

    /**
     * Группирует элементы по полю coordinates
     * @see app.core.City#coordinates
     */
    Response groupCountingByCoordinates();
}
