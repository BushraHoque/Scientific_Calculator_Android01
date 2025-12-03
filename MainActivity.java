package edu.ewubd.calculatorassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    // Buttons
    Button bAC, bC, bopen, bclose, bpercent;
    Button bsin, bcos, btan, blog, bln;
    Button bfact, bsquare, bsqrt, binv, bpi;
    Button b7, b8, b9, bmul, bpow;
    Button b4, b5, b6, bminus, bmod;
    Button b1, b2, b3, bplus, bans;
    Button b0, bdot, bequal, bdiv, bplusminus;
    Button btnHistory; // NEW: open history

    TextView tvsec, tvmain;
    String pi = "3.141592653589793";

    // SharedPreferences
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // findViewById
        bAC = findViewById(R.id.bAC);
        bC = findViewById(R.id.bC);
        bopen = findViewById(R.id.bopen);
        bclose = findViewById(R.id.bclose);
        bpercent = findViewById(R.id.bpercent);

        bsin = findViewById(R.id.bsin);
        bcos = findViewById(R.id.bcos);
        btan = findViewById(R.id.btan);
        blog = findViewById(R.id.blog);
        bln = findViewById(R.id.bln);

        bfact = findViewById(R.id.bfact);
        bsquare = findViewById(R.id.bsquare);
        bsqrt = findViewById(R.id.bsqrt);
        binv = findViewById(R.id.binv);
        bpi = findViewById(R.id.bpi);

        b7 = findViewById(R.id.b7);
        b8 = findViewById(R.id.b8);
        b9 = findViewById(R.id.b9);
        bmul = findViewById(R.id.bmul);
        bpow = findViewById(R.id.bpow);

        b4 = findViewById(R.id.b4);
        b5 = findViewById(R.id.b5);
        b6 = findViewById(R.id.b6);
        bminus = findViewById(R.id.bminus);
        bmod = findViewById(R.id.bmod);

        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        bplus = findViewById(R.id.bplus);
        bans = findViewById(R.id.bans);

        b0 = findViewById(R.id.b0);
        bdot = findViewById(R.id.bdot);
        bequal = findViewById(R.id.bequal);
        bdiv = findViewById(R.id.bdiv);
        bplusminus = findViewById(R.id.bplusminus);

        btnHistory = findViewById(R.id.btnHistory); // NEW

        tvmain = findViewById(R.id.tvmain);
        tvsec = findViewById(R.id.tvsec);

        tvmain.setText("");

        // ---------------- SharedPreferences INIT ----------------
        prefs = getSharedPreferences("calculator_history", MODE_PRIVATE);
        editor = prefs.edit();
        // --------------------------------------------------------

        // Number buttons
        View.OnClickListener numberListener = v -> {
            Button b = (Button) v;
            tvmain.append(b.getText());
        };

        b0.setOnClickListener(numberListener);
        b1.setOnClickListener(numberListener);
        b2.setOnClickListener(numberListener);
        b3.setOnClickListener(numberListener);
        b4.setOnClickListener(numberListener);
        b5.setOnClickListener(numberListener);
        b6.setOnClickListener(numberListener);
        b7.setOnClickListener(numberListener);
        b8.setOnClickListener(numberListener);
        b9.setOnClickListener(numberListener);
        bdot.setOnClickListener(numberListener);

        // Parentheses
        bopen.setOnClickListener(v -> tvmain.append("("));
        bclose.setOnClickListener(v -> tvmain.append(")"));

        // Clear all
        bAC.setOnClickListener(v -> {
            tvmain.setText("");
            tvsec.setText("");
            // Clear history
            editor.remove("history_set");
            editor.apply();
        });

        // Backspace
        bC.setOnClickListener(v -> {
            String cur = tvmain.getText().toString();
            if (cur.length() > 0) tvmain.setText(cur.substring(0, cur.length() - 1));
        });

        // Operators
        bplus.setOnClickListener(v -> tvmain.append("+"));
        bminus.setOnClickListener(v -> tvmain.append("-"));
        bmul.setOnClickListener(v -> tvmain.append("*"));
        bdiv.setOnClickListener(v -> tvmain.append("/"));
        bpercent.setOnClickListener(v -> tvmain.append("%"));
        bmod.setOnClickListener(v -> tvmain.append("mod"));
        bpow.setOnClickListener(v -> tvmain.append("^"));

        // Scientific functions
        bsin.setOnClickListener(v -> tvmain.append("sin("));
        bcos.setOnClickListener(v -> tvmain.append("cos("));
        btan.setOnClickListener(v -> tvmain.append("tan("));
        blog.setOnClickListener(v -> tvmain.append("log("));
        bln.setOnClickListener(v -> tvmain.append("ln("));
        bsqrt.setOnClickListener(v -> tvmain.append("sqrt("));
        bfact.setOnClickListener(v -> tvmain.append("!"));
        bsquare.setOnClickListener(v -> tvmain.append("^2"));
        binv.setOnClickListener(v -> tvmain.append("^(-1)"));
        bpi.setOnClickListener(v -> tvmain.append(pi));

        // +/- toggle
        bplusminus.setOnClickListener(v -> {
            String cur = tvmain.getText().toString();
            if (cur.isEmpty()) tvmain.setText("-");
            else {
                int i = cur.length() - 1;
                while (i >= 0 && (Character.isDigit(cur.charAt(i)) || cur.charAt(i) == '.')) i--;
                String before = cur.substring(0, i + 1);
                String number = cur.substring(i + 1);
                if (number.startsWith("-")) number = number.substring(1);
                else number = "-" + number;
                tvmain.setText(before + number);
            }
        });

        // Equal button
        bequal.setOnClickListener(v -> {
            String expr = tvmain.getText().toString();
            if (expr.isEmpty()) return;

            expr = expr.replace("mod", "%");

            while (expr.length() > 0 && isOperator(expr.charAt(expr.length() - 1)))
                expr = expr.substring(0, expr.length() - 1);

            try {
                double result = eval(expr);
                if (Double.isNaN(result) || Double.isInfinite(result)) {
                    tvmain.setText("");
                    Toast.makeText(MainActivity.this, "Math error", Toast.LENGTH_SHORT).show();
                    return;
                }

                String out = (result == (long) result) ? String.valueOf((long) result) : String.valueOf(result);

                tvsec.setText(expr);
                tvmain.setText(out);

                // ---------------- Save to SharedPreferences (Set) ----------------
                Set<String> set = prefs.getStringSet("history_set", new HashSet<>());
                set.add(expr + " = " + out);
                editor.putStringSet("history_set", set);
                editor.apply();
                // --------------------------------------------------------------

            } catch (Exception ex) {
                tvsec.setText(expr);
                tvmain.setText("");
                Toast.makeText(MainActivity.this, "Syntax or Math error", Toast.LENGTH_SHORT).show();
            }
        });

        // ---------------- Open History ----------------
        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, History.class);
            startActivity(intent);
        });

    } // END onCreate

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '%' || c == '!';
    }

    private static long factorialLong(long n) {
        if (n < 0) throw new ArithmeticException("Factorial of negative");
        long res = 1;
        for (long i = 2; i <= n; i++) res *= i;
        return res;
    }

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() { ch = (++pos < str.length()) ? str.charAt(pos) : -1; }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) { nextChar(); return true; }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else if (eat('%')) x %= parseFactor();
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

                double x;
                int startPos = this.pos;

                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, pos));
                } else if (ch >= 'a' && ch <= 'z') {
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, pos);
                    double arg;
                    if (eat('(')) {
                        arg = parseExpression();
                        eat(')');
                    } else arg = parseFactor();
                    x = applyFunc(func, arg);
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                while (eat('!')) {
                    if (x != Math.floor(x) || x < 0)
                        throw new RuntimeException("Invalid factorial");
                    x = factorialLong((long) x);
                }

                if (eat('^')) x = Math.pow(x, parseFactor());
                return x;
            }

            double applyFunc(String func, double x) {
                switch (func) {
                    case "sqrt": return Math.sqrt(x);
                    case "sin": return Math.sin(Math.toRadians(x));
                    case "cos": return Math.cos(Math.toRadians(x));
                    case "tan": return Math.tan(Math.toRadians(x));
                    case "log": return Math.log10(x);
                    case "ln": return Math.log(x);
                }
                throw new RuntimeException("Unknown function: " + func);
            }

        }.parse();
    }
}
