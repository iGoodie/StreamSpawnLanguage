package net.programmer.igoodie.exampleplugin.lambda;

@FunctionalInterface
public interface NumberNToNumberFunction {

    Number eval(Number... numbers);

}
