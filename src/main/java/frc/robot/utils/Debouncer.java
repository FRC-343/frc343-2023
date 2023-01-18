package frc.robot.utils;

import edu.wpi.first.wpilibj.Timer;

public class Debouncer {
    private final Timer m_timer = new Timer();

    private final double m_period;

    private boolean m_lastVal = false;
    private boolean m_debouncing = false;

    public Debouncer() {
        this(0.5);
    }

    public Debouncer(double period) {
        m_period = period;
    }

    public boolean isReady(boolean val) {
        boolean ready = true;

        if (val != m_lastVal) {
            m_timer.reset();
            m_timer.start();
            m_debouncing = true;

            ready = false;
        } else {
            if (m_debouncing) {
                double time = m_timer.get();
                if (time >= m_period) {
                    m_timer.stop();
                    m_debouncing = false;
                    ready = true;
                } else {
                    ready = false;
                }
            } else {
                ready = true;
            }
        }

        if (ready) {
            m_lastVal = val;
        }

        return ready;
    }
}