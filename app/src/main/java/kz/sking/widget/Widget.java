package kz.sking.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.RemoteViews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Widget extends AppWidgetProvider {

    public static StringBuilder fl = new StringBuilder();
    public static StringBuilder sl = new StringBuilder();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        new GetLines().execute();
        int ids = appWidgetIds.length;
        for (int i = 0; i < ids; i++) {
            int id = appWidgetIds[i];
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_widget);
            views.setTextViewText(R.id.tv1, fl.toString());
            views.setTextViewText(R.id.tv2, sl.toString());
            appWidgetManager.updateAppWidget(id, views);
        }
    }

    class GetLines extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            SimpleDateFormat df = new SimpleDateFormat("dd.MM");
            Date date = new Date();
            String today = df.format(date);
            String firstLine = String.format(" Стоимость криптовалюты на сегодня (%s):\n", today);
            fl.setLength(0);
            fl.append(firstLine);
            sl.setLength(0);
            sl.append(getRatesData());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
    public static String getRatesData() {
        StringBuilder data = new StringBuilder();
        try {
            data.append("Bitcoin:  " + getRates("bitcoin"));
            data.append("Ethereum: " + getRates("ethereum"));
            data.append("Ripple:   " + getRates("ripple"));
            data.append("EOS:      " + getRates("eos"));
            data.append("Litecoin: " + getRates("litecoin"));
        } catch (Exception ignored) {
            return null; // При ошибке доступа возвращаем null
        }
        return data.toString(); // Возвращаем результат

    }

    public static String getRates (String url) throws IOException {
        String BASE_URL = "https://ru.investing.com/crypto/" + url;
        Document doc = Jsoup.connect(BASE_URL).get();
        Elements e = doc.select("span#last_last");
        return(e.text() + " USD\n\n");
    }
}
