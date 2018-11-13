/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quadpro;

/**
 *
 * @author gsgur
 */
public class Stats{
    
    /**
     *
     * @param dataPoints
     * @return
     */
    public static <S extends Number>  Double mean(S dataPoints[])
    {
        Double sum = 0.0;
        int N  = 0;
        
        for(S element : dataPoints)
        {
            sum = element.doubleValue() + sum;
            N++;
        }
        
        return (sum / N);
    }
    
    public static <S extends Number>  Double[] StandardDeviation(S dataPoints[])
    {
        
        Double mean =  mean(dataPoints);
        
        Double sum = 0.0;
        Double SD[] = new Double[2];
        int N = 0;
        
        for(S element : dataPoints)
        {
            sum = sum + Math.pow((element.doubleValue() - mean), 2);
            N++;
        }
        
        SD[0] = Math.pow(sum / (N - 1), 0.5);  // Sample standard deviation
        SD[1] = Math.pow(sum / N, 0.5);  // Total standard deviation 
        
        return SD;
        
        
    }
    
    
    public static double map_values(double from_min, double from_max, double to_min, double to_max, double value)
    {
        double from_range = from_max - from_min;
        double to_range = to_max - to_min;
        
        return (to_range / from_range) * value;
    }
    
}
