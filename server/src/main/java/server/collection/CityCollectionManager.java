package server.collection;

import core.City;
import exceptions2.CityInteractionException;
import exceptions2.CityNotExistsException;
import interaction.Response;
import interaction.ResponseStatus;
import server.io.ClientComManager;
import server.io.Console;
import server.io.FileManager;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CityCollectionManager implements CollectionManager<City>{
    /** Дата создания */
    private Date creationDate;

    /**
     * Класс осуществляющий ввод-вывод
     */
    private ClientComManager clientComManager;

    /**
     * Коллекция, которой управляет класс
     */
    private ArrayList<City> cityCollection = new ArrayList<>();

    /**
     * id хранимых элементов
     */
    private HashSet<Long> collectionIdSet = new HashSet<>();

    /**
     * Класс, осуществляющий сохранение и загрузку коллекции
     */
    private FileManager fileManager = new FileManager();

    String collectionManagerPath = "saves.json";

    public CityCollectionManager(){
        this(null);
    }

    public CityCollectionManager(ClientComManager clientComManager){
        creationDate = new Date();
        this.clientComManager = clientComManager;
    }

    @Override
    public long createUniqueId() {
        if (cityCollection.isEmpty()){
            collectionIdSet.add(1L);
            return 1;
        }
        else{
            Long id  = cityCollection.get(cityCollection.size() - 1).getId();
            while (collectionIdSet.contains(id)){
                id++;
            }
            collectionIdSet.add(id);
            return id;
        }
    }

    @Override
    public void setCommunicationManager(ClientComManager communicationManager) {
        clientComManager = communicationManager;
    }

    @Override
    public void setCollection(ArrayList<City> collection) {
        collectionIdSet.clear();
        cityCollection.clear();
        // TODO
        /*for (City city: collection){
            if (collectionIdSet.contains(city.getId())){
                clientComManager.printErr("Элемент с id " + city.getId() + " уже есть в коллекции, текущий будет пропущен");
            }
            else {
                collectionIdSet.add(city.getId());
                cityCollection.add(city);
            }
        }
        clientComManager.printlnForce("Файл считан");*/
    }

    @Override
    public int getIndexById(long id) throws CityNotExistsException {
        for (int i = 0; i < cityCollection.size(); i++){
            if (id == cityCollection.get(i).getId()){
                return i;
            }
        }
        throw new CityNotExistsException("В коллекции нет элемента с id " + id);
    }

    @Override
    public Response showCollectionInfo() {
        return new Response(ResponseStatus.SUCCESS, "ArrayList of Cities\nCreation Date: " + creationDate + "\n" +
                                                    "Number of elements: " + cityCollection.size());
    }

    @Override
    public Response addElement() throws IOException, ClassNotFoundException {
        City city;
        try {
            city = clientComManager.readCity();
        }
        catch (CityInteractionException e){
            return new Response(ResponseStatus.ERROR, "не удалось добавить элемент. " + e.getMessage());
        }
        city.setId(createUniqueId());
        cityCollection.add(city);
        sort();

        return new Response(ResponseStatus.SUCCESS, "Элемент успешно добавлен");
    }

    @Override
    public Response update(long id) throws IOException, ClassNotFoundException {
        int cityIndex;

        try {
            cityIndex = getIndexById(id);
        }
        catch (CityNotExistsException e){
            return new Response(ResponseStatus.ERROR, e.getMessage());
        }

        City newCity;

        try{
            newCity = clientComManager.readCity();
        }
        catch (CityInteractionException e){
            return new Response(ResponseStatus.ERROR, "не удалось считать элемент. " + e.getMessage());
        }

        newCity.setId(id);
        cityCollection.set(cityIndex, newCity);
        return new Response(ResponseStatus.SUCCESS, "Элемент успешно обновлён");
    }

    @Override
    public Response update(String id) throws IOException, ClassNotFoundException {
        try {
            return update(Long.parseLong(id));
        }
        catch (NumberFormatException e){
            return new Response(ResponseStatus.ERROR, "введён id не являющийся целым числом");
        }
    }

    @Override
    public void sort() {
        Collections.sort(cityCollection);
    }

    @Override
    public Response printElements() {
        if (cityCollection.isEmpty()){
            return new Response(ResponseStatus.SUCCESS, "Коллекция пуста");
        }
        else{
            return new Response(ResponseStatus.SUCCESS,
                    cityCollection.stream().map(City::toString).collect(Collectors.joining("\n")));
        }
    }

    /**
     * Удаляет элемент коллекции
     * @param id id удаляемого элемента
     */
    public Response removeById(long id){
        int index;

        try {
            index = getIndexById(id);
        }
        catch (CityNotExistsException e){
            return new Response(ResponseStatus.ERROR, e.getMessage());
        }

        cityCollection.remove(index);

        return new Response(ResponseStatus.SUCCESS, "Элемент успешно удалён");
    }

    @Override
    public Response removeById(String id) {
        try {
            return removeById(Long.parseLong(id));
        }
        catch (NumberFormatException e){
            return new Response(ResponseStatus.ERROR, "введён id не являющийся целым числом");
        }
    }

    @Override
    public Response clearCollection() {
        cityCollection.clear();
        return new Response(ResponseStatus.SUCCESS, "Коллекция успешно очищена");
    }

    @Override
    public Response reorder() {
        Collections.reverse(cityCollection);
        return new Response(ResponseStatus.SUCCESS, "Коллекция отсортирована в порядке, обратном нынешнему");
    }

    @Override
    public Response shuffle() {
        Collections.shuffle(cityCollection);
        return new Response(ResponseStatus.SUCCESS, "Элементы коллекции перемешаны в случайном порядке");
    }

    @Override
    public Response addIfMax() throws IOException, ClassNotFoundException {
        if (cityCollection.size() == 0){
            return addElement();
        }
        else {
            City maxCity = cityCollection.stream().max(City::compareTo).get();
            City city;
            try {
                city = clientComManager.readCity();
            }
            catch (CityInteractionException e){
                return new Response(ResponseStatus.ERROR,
                                    "не удалось считать элемент. " + e.getMessage());
            }

            if (city.compareTo(maxCity) > 0){
                city.setId(createUniqueId());
                cityCollection.add(city);
                sort();
                return new Response(ResponseStatus.SUCCESS,
                                    "Элемент успешно добавлен");
            }
            else {
                return new Response(ResponseStatus.SUCCESS,
                                    "Элемент не больше максимального элемента коллекции");
            }
        }
    }

    @Override
    public Response minByCreationDate() {
        if(cityCollection.size() == 0){
            return new Response(ResponseStatus.SUCCESS, "Коллекция пуста");
        }
        else{
            return new Response(ResponseStatus.SUCCESS, cityCollection.stream().min(City::compareTo).toString());
        }
    }

    @Override
    public Response filterGreaterThanSeaLevel(String metersAboveSeaLevel) {
        try {
            return filterGreaterThanSeaLevel(Long.parseLong(metersAboveSeaLevel));
        }
        catch (NumberFormatException e){
            return new Response(ResponseStatus.ERROR, "Введено не целое число");
        }
    }

    /**
     * Выводит элементы, значение поля которых, превышает данное
     * @param metersAboveSeaLevel сравниваемое значение
     * @see app.core.City#metersAboveSeaLevel
     */
    public Response filterGreaterThanSeaLevel(long metersAboveSeaLevel) {
        if (cityCollection.size() == 0){
            return new Response(ResponseStatus.SUCCESS, "Коллекция пуста");
        }
        else {
            try {
                return new Response(ResponseStatus.SUCCESS, cityCollection.stream()
                        .filter((a) -> a.getMetersAboveSeaLevel() != null & a.getMetersAboveSeaLevel() > metersAboveSeaLevel)
                        .findFirst()
                        .get().toString());
            }
            catch (NullPointerException e) {
                return new Response(ResponseStatus.SUCCESS,
                        "В коллекции нет элементов со значением поля metersAboveSeaLevel больше заданного");
            }
        }
    }

    @Override
    public Response groupCountingByCoordinates() {
        if (cityCollection.size() == 0){
            return new Response(ResponseStatus.SUCCESS, "Коллекция пуста");
        }
        else {
            Map<Integer, Long> groups = cityCollection.stream().collect(Collectors.groupingBy(City::getQuarter, Collectors.counting()));

            StringBuilder res = new StringBuilder();
            res.append("Распределение городов по четвертям:\n");

            for (Map.Entry<Integer, Long> entry: groups.entrySet()){
                res.append(entry.getKey().toString()).append(": ").append(entry.getValue()).append("\n");
            }

            return new Response(ResponseStatus.SUCCESS, res.toString());
        }
    }

    @Override
    public void save(String path) {
        try {
            fileManager.save(cityCollection, path);
            Console.print("Коллекция сохранена в файл " + path);
        }
        catch (NullPointerException e){
            save(collectionManagerPath);
        }
        catch (IOException e){
            Console.printErr("не удалось сохранить коллекцию в файл\n" + e.getMessage());
            String newPath = Console.readPath();

            if (newPath.length() == 0){
                return;
            }
            save(newPath);
        }
    }
}
