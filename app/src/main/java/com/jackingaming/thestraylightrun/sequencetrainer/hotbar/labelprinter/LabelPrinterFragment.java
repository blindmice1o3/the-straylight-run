package com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jackingaming.thestraylightrun.R;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.cupcaddy.CupCaddyFragment;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.AdapterDrink;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.Drink;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.SimpleItemTouchHelperCallback;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.networking.adapters.LocalDateTimeTypeAdapter;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.networking.dtos.OrderDTO;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.networking.models.MenuItemInfo;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.networking.models.Order;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.LabelPrinter;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.DrinkComponent;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.EspressoShot;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Ice;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Milk;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.drinkcomponents.Syrup;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class LabelPrinterFragment extends Fragment {
    public static final String TAG = LabelPrinterFragment.class.getSimpleName();

    public static final String URL_ORDERS = "http://192.168.1.143:8080/v1/orders";
    public static final long DELAY_DURATION_IN_SECONDS = 3L;

    private RequestQueue requestQueue;
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private ScheduledFuture requestRepeatingFetchNewerOrders;
    private LocalDateTime timestampLastDrink;

    public interface Listener {
        void onInitializationCompleted(LabelPrinter labelPrinter);
    }

    private Listener listener;

    private ConstraintLayout constraintLayoutLabelPrinter;
    private LabelPrinter labelPrinter;
    private List<Drink> queueDrinks;
    private RecyclerView recyclerViewQueueDrinks;
    private AdapterDrink adapter;

    public LabelPrinterFragment() {
        // Required empty public constructor
    }

    public static LabelPrinterFragment newInstance() {
        Log.e(TAG, "newInstance()");
        LabelPrinterFragment fragment = new LabelPrinterFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (Listener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement InitializationListener");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate()");

        requestQueue = Volley.newRequestQueue(getContext());
        timestampLastDrink = LocalDateTime.now();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView()");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_label_printer, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated()");

        constraintLayoutLabelPrinter = view.findViewById(R.id.constraintlayout_label_printer);
        labelPrinter = view.findViewById(R.id.tv_label_printer);
        queueDrinks = labelPrinter.getQueueDrinks();
        recyclerViewQueueDrinks = view.findViewById(R.id.rv_queue_drinks);

        adapter = new AdapterDrink(queueDrinks, new AdapterDrink.UpdateListener() {
            @Override
            public void updateDisplay() {
                labelPrinter.updateDisplay();
            }
        });
        recyclerViewQueueDrinks.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewQueueDrinks.setLayoutManager(linearLayoutManager);
        recyclerViewQueueDrinks.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerViewQueueDrinks);

        labelPrinter.setListener(new LabelPrinter.LabelPrinterListener() {
            @Override
            public void onDrinkAdded(int indexToAdd) {
                adapter.notifyItemInserted(indexToAdd);
            }

            @Override
            public void onDrinkRemoved(int indexToRemove) {
                adapter.notifyItemRemoved(indexToRemove);
            }

            @Override
            public void onLastDrinkRemoved(Drink drink) {
                String textForDrinkLabel = drink.getTextForDrinkLabel();
                String[] textForDrinkLabelSplitted = textForDrinkLabel.split("\\s+");
                String textDate = textForDrinkLabelSplitted[0];
                String textTime = textForDrinkLabelSplitted[1];
                String textAmOrPm = textForDrinkLabelSplitted[2];
//                                String textSize = textForDrinkLabelSplitted[3];
//                                String textDrink = textForDrinkLabelSplitted[4];

                // convert String timestamp into LocalDateTime.
                String dateTimeString = textDate + " " + textTime + " " + textAmOrPm;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");

                // store timestamp of last drink from queue (before the last label is pulled).
                //   used to check db-service for drinks that are more recent.
                timestampLastDrink = LocalDateTime.parse(dateTimeString, formatter);
            }
        });

        labelPrinter.generateRandomDrinkRequest();
        labelPrinter.updateDisplay();

        listener.onInitializationCompleted(labelPrinter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume()");

        // pull from server (VesselForCheeseDBService).
        requestRepeatingFetchNewerOrders = executor.scheduleWithFixedDelay(
                new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void run() {
                        try {
                            LocalDateTime timestampNewest = LocalDateTime.of(1942, 4, 20, 16, 20, 0);
                            // queue NOT empty, use LAST drink's timestamp.
                            if (!queueDrinks.isEmpty()) {
//                                Log.e(TAG, "drink queue NOT empty");
                                int indexEnd = queueDrinks.size() - 1;
                                String textForDrinkLabel = queueDrinks.get(indexEnd).getTextForDrinkLabel();
                                String[] textForDrinkLabelSplitted = textForDrinkLabel.split("\\s+");
                                String textDate = textForDrinkLabelSplitted[0];
                                String textTime = textForDrinkLabelSplitted[1];
                                String textAmOrPm = textForDrinkLabelSplitted[2];
//                                String textSize = textForDrinkLabelSplitted[3];
//                                String textDrink = textForDrinkLabelSplitted[4];

                                // convert String timestamp into LocalDateTime.
                                String dateTimeString = textDate + " " + textTime + " " + textAmOrPm;
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
                                timestampNewest = LocalDateTime.parse(dateTimeString, formatter);
//                                Log.e(TAG, "timestampNewest: " + timestampNewest.toString());
                            }
                            // queue IS empty, use timestampLastDrink.
                            else {
//                                Log.e(TAG, "drink queue empty");
                                timestampNewest = timestampLastDrink;
                            }

                            requestFetchNewerOrders(
                                    timestampNewest
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                0,
                DELAY_DURATION_IN_SECONDS,
                TimeUnit.SECONDS);
    }

    @Override
    public void onDestroyView() {
        requestQueue.stop();
        super.onDestroyView();
        Log.e(TAG, "onDestroyView()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop()");
        requestRepeatingFetchNewerOrders.cancel(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void requestFetchNewerOrders(LocalDateTime localDateTime) throws JSONException {
//        Log.i(TAG, "requestFetchNewerOrders(LocalDateTime)");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String timestampAsString = localDateTime.format(formatter);
//        Log.e(TAG, "timestampAsString: " + timestampAsString);

        String templateWithRequestParam = URL_ORDERS + "?timestamp=%s";
        String urlFetchNewerOrders = String.format(templateWithRequestParam, timestampAsString);
        JsonObjectRequest fetchNewerOrderRequest = new JsonObjectRequest(
                Request.Method.GET,
                urlFetchNewerOrders,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Log.i(TAG, "JsonObjectRequest onResponse(JSONObject)");

                        Gson gson = new GsonBuilder()
                                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                                .create();
                        OrderDTO orderDTO = gson.fromJson(response.toString(), OrderDTO.class);

                        List<Order> ordersFromServer = orderDTO.getOrders();

                        if (!ordersFromServer.isEmpty()) {
//                            Log.e(TAG, "NOT ordersFromServer.isEmpty()... addAll()");

                            List<Drink> drinksConvertedFromOrder = new ArrayList<>();
                            for (Order order : ordersFromServer) {
                                LocalDateTime createdOn = order.getCreatedOn();
                                List<MenuItemInfo> menuItemInfos = order.getMenuItemInfos();
                                for (MenuItemInfo menuItemInfo : menuItemInfos) {
                                    String id = menuItemInfo.getId();
                                    String size = menuItemInfo.getSize();
                                    List<String> customizations = menuItemInfo.getMenuItemCustomizations();
                                    Log.e(TAG, createdOn.toString());
                                    String textCustomizations = (customizations.isEmpty()) ? "standard" : "customized";
                                    Log.e(TAG, id + " | " + size + " | " + textCustomizations);

                                    Drink drinkToAdd = Menu.getDrinkByName(id);

                                    // set size for drink
                                    Drink.Size sizeToAdd = null;
                                    if (size.equals("Short")) {
                                        sizeToAdd = Drink.Size.SHORT;
                                    } else if (size.equals("Tall")) {
                                        sizeToAdd = Drink.Size.TALL;
                                    } else if (size.equals("Grande")) {
                                        sizeToAdd = Drink.Size.GRANDE;
                                    } else if (size.equals("Venti")) {
                                        sizeToAdd = Drink.Size.VENTI_HOT;
                                    } else if (size.equals("Trenta")) {
                                        sizeToAdd = Drink.Size.TRENTA;
                                    }
                                    drinkToAdd.setSize(sizeToAdd);

                                    // initialize drinkComponents for drink
                                    drinkToAdd.getDrinkComponentsBySize(sizeToAdd);

                                    // TODO: handle customizations.
                                    int counter = 0;
                                    String textCustomizationForLabel = "";
                                    for (String customization : customizations) {
                                        Log.e(TAG, counter + ": " + customization);
                                        if (customization.substring(0, 5).equals("Syrup") ||
                                                customization.substring(0, 5).equals("Sauce")) {
                                            Log.e(TAG, "SYRUP || SAUCE");

                                            String[] customizationSplitted = customization.split("\\s+");
                                            int numberOfPumpsCustomized = Integer.parseInt(customizationSplitted[2]);
                                            if (customizationSplitted[1].equals("VANILLA_SYRUP,")) {
                                                textCustomizationForLabel += "\n" + numberOfPumpsCustomized + " VANILLA";
                                                handleAddOrRemoveSyrup(numberOfPumpsCustomized,
                                                        Syrup.Type.VANILLA, drinkToAdd.getDrinkComponents());
                                            }
                                            if (customizationSplitted[1].equals("MOCHA_SAUCE,")) {
                                                textCustomizationForLabel += "\n" + numberOfPumpsCustomized + " MOCHA";
                                                handleAddOrRemoveSyrup(numberOfPumpsCustomized,
                                                        Syrup.Type.MOCHA, drinkToAdd.getDrinkComponents());
                                            }
                                            if (customizationSplitted[1].equals("BROWN_SUGAR_SYRUP,")) {
                                                textCustomizationForLabel += "\n" + numberOfPumpsCustomized + " BROWN_SUGAR";
                                                handleAddOrRemoveSyrup(numberOfPumpsCustomized,
                                                        Syrup.Type.BROWN_SUGAR, drinkToAdd.getDrinkComponents());
                                            }
                                        } else if (customization.substring(0, 8).equals("MilkBase")) {
                                            Log.e(TAG, "MILK_BASE");

                                            Milk milkStandard = null;
                                            for (DrinkComponent drinkComponent : drinkToAdd.getDrinkComponents()) {
                                                if (drinkComponent instanceof Milk) {
                                                    milkStandard = (Milk) drinkComponent;
                                                    break;
                                                }
                                            }

                                            Milk.Type type = null;
                                            String[] customizationSplitted = customization.split("\\s+");
                                            if (customizationSplitted[1].equals("TWO_PERCENT,")) {
                                                textCustomizationForLabel += "\nTWO_PERCENT";
                                                type = Milk.Type.TWO_PERCENT;
                                            } else if (customizationSplitted[1].equals("WHOLE_MILK,")) {
                                                textCustomizationForLabel += "\nWHOLE";
                                                type = Milk.Type.WHOLE;
                                            } else if (customizationSplitted[1].equals("OATMILK,")) {
                                                textCustomizationForLabel += "\nOAT";
                                                type = Milk.Type.OAT;
                                            } else if (customizationSplitted[1].equals("COCONUT,")) {
                                                textCustomizationForLabel += "\nCOCONUT";
                                                type = Milk.Type.COCONUT;
                                            } else if (customizationSplitted[1].equals("ALMOND,")) {
                                                textCustomizationForLabel += "\nALMOND";
                                                type = Milk.Type.ALMOND;
                                            } else if (customizationSplitted[1].equals("SOY,")) {
                                                textCustomizationForLabel += "\nSOY";
                                                type = Milk.Type.SOY;
                                            }

                                            if (milkStandard != null) {
                                                milkStandard.setType(type);
                                            } else {
                                                Log.e(TAG, "milkStandard == null... meaning did not find milk in standard recipe.");
                                            }
                                        } else if (customization.substring(0, 7).equals("CupSize")) {
                                            Log.e(TAG, "CUP_SIZE");

                                            boolean isIced = false;
                                            for (DrinkComponent drinkComponent : drinkToAdd.getDrinkComponents()) {
                                                if (drinkComponent instanceof Ice) {
                                                    isIced = true;
                                                    break;
                                                }
                                            }

                                            String[] customizationSplitted = customization.split("\\s+");
                                            if (customizationSplitted[1].equals("VENTI,")) {
                                                String cupSizeSpecified = (isIced) ? CupCaddyFragment.TAG_COLD_VENTI : CupCaddyFragment.TAG_HOT_VENTI;
                                                textCustomizationForLabel += "\ncup: " + cupSizeSpecified;

                                                drinkToAdd.getDrinkProperties().put(Drink.Property.CUP_SIZE_SPECIFIED, cupSizeSpecified);
                                            } else if (customizationSplitted[1].equals("GRANDE,")) {
                                                String cupSizeSpecified = (isIced) ? CupCaddyFragment.TAG_COLD_GRANDE : CupCaddyFragment.TAG_HOT_GRANDE;
                                                textCustomizationForLabel += "\ncup: " + cupSizeSpecified;

                                                drinkToAdd.getDrinkProperties().put(Drink.Property.CUP_SIZE_SPECIFIED, cupSizeSpecified);
                                            } else if (customizationSplitted[1].equals("TALL,")) {
                                                String cupSizeSpecified = (isIced) ? CupCaddyFragment.TAG_COLD_TALL : CupCaddyFragment.TAG_HOT_TALL;
                                                textCustomizationForLabel += "\ncup: " + cupSizeSpecified;

                                                drinkToAdd.getDrinkProperties().put(Drink.Property.CUP_SIZE_SPECIFIED, cupSizeSpecified);
                                            } else if (customizationSplitted[1].equals("SHORT,")) {
                                                String cupSizeSpecified = CupCaddyFragment.TAG_HOT_SHORT;
                                                textCustomizationForLabel += "\ncup: " + cupSizeSpecified;

                                                drinkToAdd.getDrinkProperties().put(Drink.Property.CUP_SIZE_SPECIFIED, cupSizeSpecified);
                                            } else if (customizationSplitted[1].equals("TRENTA,")) {
                                                String cupSizeSpecified = CupCaddyFragment.TAG_COLD_TRENTA;
                                                textCustomizationForLabel += "\ncup: " + cupSizeSpecified;

                                                drinkToAdd.getDrinkProperties().put(Drink.Property.CUP_SIZE_SPECIFIED, cupSizeSpecified);
                                            }
                                        } else if (customization.substring(0, 12).equals("RoastOptions")) {
                                            Log.e(TAG, "ROAST_OPTIONS");

                                            String[] customizationSplitted = customization.split("\\s+");
                                            EspressoShot.Type type = null;
                                            if (customizationSplitted[1].equals("BLONDE,")) {
                                                Log.e(TAG, "BLONDE");
                                                textCustomizationForLabel += "\n" + "BLONDE";
                                                type = EspressoShot.Type.BLONDE;
                                            } else if (customizationSplitted[1].equals("DECAF,")) {
                                                Log.e(TAG, "DECAF");
                                                textCustomizationForLabel += "\n" + "DECAF";
                                                type = EspressoShot.Type.DECAF;
                                            }
//                                            else if (customizationSplitted[1].equals("DECAF_ONE_THIRD")) {
//                                                Log.e(TAG, "DECAF_ONE_THIRD");
//                                            } else if (customizationSplitted[1].equals("DECAF_ONE_HALF")) {
//                                                Log.e(TAG, "DECAF_ONE_HALF");
//                                            } else if (customizationSplitted[1].equals("DECAF_TWO_THIRD")) {
//                                                Log.e(TAG, "DECAF_TWO_THIRD");
//                                            }

                                            for (DrinkComponent drinkComponent : drinkToAdd.getDrinkComponents()) {
                                                if (drinkComponent instanceof EspressoShot) {
                                                    ((EspressoShot) drinkComponent).setType(type);
                                                }
                                            }
                                        } else if (customization.substring(0, 5).equals("Shots")) {
                                            Log.e(TAG, "SHOTS");

                                            String[] customizationSplitted = customization.split("\\s+");
                                            int numberOfShotsCustomized = Integer.parseInt(customizationSplitted[2]);
                                            textCustomizationForLabel += "\n" + numberOfShotsCustomized + " shot";
                                            handleAddOrRemoveShot(numberOfShotsCustomized,
                                                    drinkToAdd.getDrinkComponents(), customizations);
                                        } else {
                                            Log.e(TAG, "NOT syrup/sauce && NOT milk base && NOT cup size && NOT roast options && NOT shots");
                                        }

                                        counter++;
                                    }

                                    // initialize textForDrinkLabel for drink
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
                                    String formatDateTime = createdOn.format(formatter);
                                    String contentNewDrinkLabel = String.format("%s\n%s\n%s\n%s",
                                            formatDateTime,
                                            "mobile",
                                            drinkToAdd.getSize(),
                                            drinkToAdd.getName());

                                    if (textCustomizationForLabel.length() > 0) {
                                        contentNewDrinkLabel += textCustomizationForLabel;
                                    }

                                    drinkToAdd.setTextForDrinkLabel(
                                            contentNewDrinkLabel
                                    );

                                    drinksConvertedFromOrder.add(drinkToAdd);
                                }
                            }

                            queueDrinks.addAll(drinksConvertedFromOrder);
                            adapter.notifyDataSetChanged();
                            labelPrinter.updateDisplay();
                        } else {
                            Log.e(TAG, "ordersFromServer.isEmpty()... do nothing");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
//                        Log.e(TAG, "onErrorResponse(VolleyError)" + error);
                    }
                }
        );

        requestQueue.add(fetchNewerOrderRequest);
    }

    private void handleAddOrRemoveShot(int numberOfShotsTotal,
                                       List<DrinkComponent> drinkComponents,
                                       List<String> customizations) {
        // count how many shots are in the standard recipe.
        int counterShot = 0;
        EspressoShot shot = null;
        int indexFirstOccurrence = -1;
        for (int i = 0; i < drinkComponents.size(); i++) {
            DrinkComponent drinkComponent = drinkComponents.get(i);
            if (drinkComponent instanceof EspressoShot) {
                counterShot++;

                if (indexFirstOccurrence < 0) {
                    indexFirstOccurrence = i;
                }
                shot = (EspressoShot) drinkComponent;
            }
        }

        // add shot(s)
        if (counterShot < numberOfShotsTotal) {
            int numberOfShotsToAdd = numberOfShotsTotal - counterShot;

            boolean isShotInStandardRecipe = (shot != null);
            // shot in standard recipe
            if (isShotInStandardRecipe) {
                for (int i = 0; i < numberOfShotsToAdd; i++) {
                    EspressoShot shotToAdd = new EspressoShot(shot.getType(),
                            shot.getAmountOfWater(),
                            shot.getAmountOfBean());
                    shotToAdd.setShaken(shot.isShaken());
                    shotToAdd.setBlended(shot.isBlended());
                    drinkComponents.add(indexFirstOccurrence, shotToAdd);
                }
            }
            // no shot in standard recipe (find milk)
            else {
                Milk milk = null;
                int indexToAdd = -1;
                for (int i = 0; i < drinkComponents.size(); i++) {
                    if (drinkComponents.get(i) instanceof Milk) {
                        milk = (Milk) drinkComponents.get(i);
                        indexToAdd = i;
                        break;
                    }
                }

                EspressoShot.Type type = null;
                EspressoShot.AmountOfWater amountOfWater = null;
                for (String customization : customizations) {
                    if (customization.substring(0, 20).equals("PullOptionsAllowable") ||
                            customization.substring(0, 11).equals("PullOptions")) {
                        Log.e(TAG, "PullOptionsAllowable || PullOptions");

                        String[] customizationSplitted = customization.split("\\s+");
                        if (customizationSplitted[1].equals("RISTRETTO,")) {
                            amountOfWater = EspressoShot.AmountOfWater.RISTRETTO;
                        } else if (customizationSplitted[1].equals("LONG,")) {
                            amountOfWater = EspressoShot.AmountOfWater.LONG;
                        }
                    } else if (customization.substring(0, 21).equals("RoastOptionsAllowable") ||
                            customization.substring(0, 12).equals("RoastOptions")) {
                        Log.e(TAG, "RoastOptionsAllowable || RoastOptions");

                        String[] customizationSplitted = customization.split("\\s+");
                        if (customizationSplitted[1].equals("BLONDE,")) {
                            Log.e(TAG, "BLONDE");
                            type = EspressoShot.Type.BLONDE;
                        } else if (customizationSplitted[1].equals("DECAF,")) {
                            Log.e(TAG, "DECAF");
                            type = EspressoShot.Type.DECAF;
                        }
                    }
                }
                if (type == null) {
                    type = EspressoShot.Type.SIGNATURE;
                }
                if (amountOfWater == null) {
                    amountOfWater = EspressoShot.AmountOfWater.STANDARD;
                }

                for (int i = 0; i < numberOfShotsToAdd; i++) {
                    EspressoShot shotToAdd = new EspressoShot(type,
                            amountOfWater,
                            EspressoShot.AmountOfBean.STANDARD);
                    shotToAdd.setShaken(milk.isShaken());
                    shotToAdd.setBlended(milk.isBlended());
                    drinkComponents.add(indexToAdd, shotToAdd);
                }
            }
        }
        // remove shot(s)
        else {
            int numberOfShotsToRemove = counterShot - numberOfShotsTotal;

            for (int i = 0; i < numberOfShotsToRemove; i++) {
                drinkComponents.remove(indexFirstOccurrence);
            }
        }
    }

    private void handleAddOrRemoveSyrup(int numberOfPumpsTotal,
                                        Syrup.Type type,
                                        List<DrinkComponent> drinkComponents) {
        // count how many pumps are in the standard recipe.
        int counterSyrup = 0;
        boolean shaken = false;
        boolean blended = false;
        for (DrinkComponent drinkComponent : drinkComponents) {
            if (drinkComponent instanceof Syrup &&
                    ((Syrup) drinkComponent).getType() == type) {
                counterSyrup++;
                shaken = drinkComponent.isShaken();
                blended = drinkComponent.isBlended();
            }
        }

        boolean isSyrupInStandardRecipe = counterSyrup > 0;

        // add pump(s)
        if (counterSyrup < numberOfPumpsTotal) {
            int numberOfPumpsToAdd = numberOfPumpsTotal - counterSyrup;

            int index = -1;
            // find index of first syrup
            if (isSyrupInStandardRecipe) {
                for (int i = 0; i < drinkComponents.size(); i++) {
                    if (drinkComponents.get(i) instanceof Syrup) {
                        index = i;
                        break;
                    }
                }
            }
            // no syrup in standard recipe (find espresso shot or milk)
            else {
                for (int i = 0; i < drinkComponents.size(); i++) {
                    if (drinkComponents.get(i) instanceof EspressoShot) {
                        index = i;
                        break;
                    } else if (drinkComponents.get(i) instanceof Milk) {
                        index = i;
                        break;
                    }
                }
            }

            for (int i = 0; i < numberOfPumpsToAdd; i++) {
                Syrup syrupToAdd = new Syrup(type);
                syrupToAdd.setShaken(shaken);
                syrupToAdd.setBlended(blended);
                drinkComponents.add(index, syrupToAdd);
            }
        }
        // remove pump(s)
        else {
            int numberOfPumpsToRemove = counterSyrup - numberOfPumpsTotal;

            int index = -1;
            // find index of first syrup
            if (isSyrupInStandardRecipe) {
                for (int i = 0; i < drinkComponents.size(); i++) {
                    if (drinkComponents.get(i) instanceof Syrup) {
                        index = i;
                        break;
                    }
                }
            }

            for (int i = 0; i < numberOfPumpsToRemove; i++) {
                drinkComponents.remove(index);
            }
        }
    }

    public void changeLabelPrinterMode(String modeSelected) {
        Log.e(TAG, "changeLabelPrinterMode(String) modeSelected: " + modeSelected);

//        if (labelPrinter == null) {
//            Toast.makeText(getContext(), "labelPrinter is null", Toast.LENGTH_SHORT).show();
//            return;
//        }

        if (modeSelected.equals("standard")) {
            labelPrinter.selectModeStandard();
//            delayAddNewDrink = DELAY_ADD_NEW_DRINK_EASY;
//            valueBracketYellow = VALUE_BRACKET_YELLOW_EASY;
//            valueBracketRed = VALUE_BRACKET_RED_EASY;
        } else if (modeSelected.equals("customized")) {
            labelPrinter.selectModeCustomized();
//            delayAddNewDrink = DELAY_ADD_NEW_DRINK_MEDIUM;
//            valueBracketYellow = VALUE_BRACKET_YELLOW_MEDIUM;
//            valueBracketRed = VALUE_BRACKET_RED_MEDIUM;
        } else if (modeSelected.equals("both")) {
            labelPrinter.selectModeBoth();
//            delayAddNewDrink = DELAY_ADD_NEW_DRINK_HARD;
//            valueBracketYellow = VALUE_BRACKET_YELLOW_HARD;
//            valueBracketRed = VALUE_BRACKET_RED_HARD;
        }
    }

    public LabelPrinter getLabelPrinter() {
        return labelPrinter;
    }

    public void setLabelPrinter(LabelPrinter labelPrinter) {
        this.labelPrinter = labelPrinter;
    }
}