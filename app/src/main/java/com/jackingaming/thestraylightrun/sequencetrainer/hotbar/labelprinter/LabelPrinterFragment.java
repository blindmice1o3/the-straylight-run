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
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.AdapterDrink;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.menuitems.drinks.Drink;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.networking.adapters.LocalDateTimeTypeAdapter;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.networking.dtos.OrderDTO;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.networking.models.MenuItemInfo;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.labelprinter.networking.models.Order;
import com.jackingaming.thestraylightrun.sequencetrainer.hotbar.mastrena.entities.LabelPrinter;

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

        adapter = new AdapterDrink(queueDrinks);
        recyclerViewQueueDrinks.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewQueueDrinks.setLayoutManager(linearLayoutManager);
        recyclerViewQueueDrinks.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

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
                                Log.e(TAG, "drink queue NOT empty");
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
                                Log.e(TAG, "timestampNewest: " + timestampNewest.toString());
                            }
                            // queue IS empty, use timestampLastDrink.
                            else {
                                Log.e(TAG, "drink queue empty");
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
        Log.i(TAG, "requestFetchNewerOrders(LocalDateTime)");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String timestampAsString = localDateTime.format(formatter);
        Log.e(TAG, "timestampAsString: " + timestampAsString);

        String templateWithRequestParam = URL_ORDERS + "?timestamp=%s";
        String urlFetchNewerOrders = String.format(templateWithRequestParam, timestampAsString);
        JsonObjectRequest fetchNewerOrderRequest = new JsonObjectRequest(
                Request.Method.GET,
                urlFetchNewerOrders,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "JsonObjectRequest onResponse(JSONObject)");

                        Gson gson = new GsonBuilder()
                                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                                .create();
                        OrderDTO orderDTO = gson.fromJson(response.toString(), OrderDTO.class);

                        List<Order> ordersFromServer = orderDTO.getOrders();

                        if (!ordersFromServer.isEmpty()) {
                            Log.e(TAG, "NOT ordersFromServer.isEmpty()... addAll()");

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

                                    // set size for drink
                                    Drink drinkToAdd = Menu.getDrinkByName(id);
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

                                    // initialize textForDrinkLabel for drink
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
                                    String formatDateTime = createdOn.format(formatter);
                                    String contentNewDrinkLabel = String.format("%s\n%s\n%s",
                                            formatDateTime,
                                            drinkToAdd.getSize(),
                                            drinkToAdd.getName());
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
                        Log.e(TAG, "onErrorResponse(VolleyError)" + error);
                    }
                }
        );

        requestQueue.add(fetchNewerOrderRequest);
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