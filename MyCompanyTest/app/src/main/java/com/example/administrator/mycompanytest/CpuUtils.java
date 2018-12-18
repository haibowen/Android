package com.example.administrator.mycompanytest;

import android.util.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CpuUtils {

    private static final String TAG = CpuUtils.class.getSimpleName();

    // 获取CPU名字
    public static String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
            }
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }




    public static int getNumCpuCores() {
        try {
            // Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            // Filter to only list the devices we care about
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    // Check if filename is "cpu", followed by a single digit number
                    if (Pattern.matches("cpu[0-9]+", file.getName())) {
                        return true;
                    }
                    return false;
                }
            });
            // Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            // Default to return 1 core
            Log.e(TAG, "Failed to count number of cores, defaulting to 1", e);
            return 1;
        }
    }

    /**
     * 64 系统判断
     *
     * @return
     */

    public static boolean isCpu64() {
        boolean result = false;
        if (BuildHelper.isCpu64() || ProcCpuInfo.isCpu64()) {
            result = true;
        }
        return result;
    }

    /**
     * CPU 最大频率
     *
     * @return
     */
    public static long getCpuMaxFreq() {
        long result = 0L;
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"));
            if ((line = br.readLine()) != null) {
                result = Long.parseLong(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * CPU 最小频率
     *
     * @return
     */
    public static long getCpuMinFreq() {
        long result = 0L;
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq"));
            if ((line = br.readLine()) != null) {
                result = Long.parseLong(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 可调节 CPU 频率档位
     *
     * @return
     */
    public static String getCpuAvailableFrequenciesSimple() {
        String result = null;
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies"));
            if( (line = br.readLine()) != null) {
                result = line;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 可调节 CPU 频率档位
     *
     * @return
     */
    public static List<Long> getCpuAvailableFrequencies() {
        List<Long> result = new ArrayList<>();
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies"));
            if( (line = br.readLine()) != null) {
                String[] list = line.split("\\s+");
                for (String value : list) {
                    long freq = Long.parseLong(value);
                    result.add(freq);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * CPU 调频策略
     *
     * @return
     */
    public static String getCpuGovernor() {
        String result = null;
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor"));
            if ((line = br.readLine()) != null) {
                result = line;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * CPU 支持的调频策略
     *
     * @return
     */
    public static String getCpuAvailableGovernorsSimple() {
        String result = null;
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors"));
            if( (line = br.readLine()) != null) {
                result = line;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * CPU 支持的调频策略
     *
     * @return
     */
    public static List<String> getCpuAvailableGovernors() {
        List<String> result = new ArrayList<>();
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors"));
            if( (line = br.readLine()) != null) {
                String[] list = line.split("\\s+");
                for (String value : list) {
                    result.add(value);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Get cpu's current frequency
     * unit:KHZ
     * 获取cpu当前频率,单位KHZ
     *
     * @return
     */
    public static List<Integer> getCpuCurFreq() {
        List<Integer> results = new ArrayList<Integer>();
        String freq = "";
        FileReader fr = null;
        try {
            int cpuIndex = 0;
            Integer lastFreq = 0;
            while (true) {
                File file = new File("/sys/devices/system/cpu/cpu" + cpuIndex + "/");
                if (!file.exists()) {
                    break;
                }
                file = new File("/sys/devices/system/cpu/cpu" + cpuIndex + "/cpufreq/");
                if (!file.exists()) {
                    lastFreq = 0;
                    results.add(0);
                    cpuIndex++;
                    continue;
                }
                file = new File("/sys/devices/system/cpu/cpu" + cpuIndex + "/cpufreq/scaling_cur_freq");
                if (!file.exists()) {
                    results.add(lastFreq);
                    cpuIndex++;
                    continue;
                }
                fr = new FileReader(
                        "/sys/devices/system/cpu/cpu" + cpuIndex + "/cpufreq/scaling_cur_freq");
                BufferedReader br = new BufferedReader(fr);
                String text = br.readLine();
                freq = text.trim();
                lastFreq = Integer.valueOf(freq);
                results.add(lastFreq);
                fr.close();
                cpuIndex++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return results;
    }

}
