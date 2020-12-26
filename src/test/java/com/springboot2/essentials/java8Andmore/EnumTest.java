package com.springboot2.essentials.java8Andmore;

import java.util.function.BiFunction;

public class EnumTest {

    /**
     * Valid only for static things. we cannot have for example an external call here (e.g: db access)!
     */
    public enum RATE{
        ONE{
            @Override
            public long computePrice(int days) {
                return days + 1;
            }
        },
        TWO{
            @Override
            public long computePrice(int days) {
                return days + 2;
            }
        },
        THREE{
            @Override
            public long computePrice(int days) {
                return days + 3;
            }
        },
        FOUR{
            @Override
            public long computePrice(int days) {
                return days + 4;
            }
        };

        public abstract long computePrice(int days);
    }

    private RATE rate;

    public long calculate(int days){
        //This replace switch-case statement
        return rate.computePrice(days);

    }

    public enum Type{
        REGULAR(PriceService::computeRegular),
        NEW_RELEASE(PriceService::computeNewRelease),
        CHILDREN(PriceService::computeChildren),
        OP((x,y) -> x.computeChildren(y));

        private BiFunction<PriceService, Integer, Integer> priceAlgo;
        Type(BiFunction<PriceService, Integer, Integer> priceAlgo){
            this.priceAlgo = priceAlgo;
        }

    }

    public class PriceService {

        public int computePrice(Type type, int days){
            return type.priceAlgo.apply(this, days);
        }

        int computeRegular(int days){
            return 2 + days;
        }

        int computeNewRelease(int days){
            return 3 + days;
        }

        int computeChildren(int days){
            return 1 + days;
        }

    }


}
