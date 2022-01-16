package com.ncs.commons.bean;

import com.vip.vjtools.vjkit.collection.ArrayUtil;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 暴露工具类,使用时
 */
public class CBeanMapper {
    private static Mapper mapper = new DozerBeanMapper();

    public static <S, D> D map(S source, Class<D> destinationClass) {
        return mapper.map(source, destinationClass);
    }


    public static <S, D> D map(S source, Class<D> destinationClass, CBeanAfterHandler<S, D> beanAfterHandler) {
        D d = mapper.map(source, destinationClass);
        beanAfterHandler.handle(source, d);
        return d;
    }


    public static <S, D> List<D> mapList(Iterable<S> sourceList, Class<D> destinationClass) {
        List<D> destionationList = new ArrayList<D>();
        for (S source : sourceList) {
            if (source != null) {
                destionationList.add(mapper.map(source, destinationClass));
            }
        }
        return destionationList;
    }

    public static <S, D> List<D> mapList(Iterable<S> sourceList, Class<D> destinationClass, CBeanAfterHandler<S, D> beanAfterHandler) {
        List<D> destionationList = new ArrayList<D>();
        for (S source : sourceList) {
            if (source != null) {
                D d = mapper.map(source, destinationClass);
                beanAfterHandler.handle(source, d);
                destionationList.add(d);
            }
        }
        return destionationList;
    }


    public static <S, D> D[] mapArray(final S[] sourceArray, final Class<D> destinationClass) {
        D[] destinationArray = ArrayUtil.newArray(destinationClass, sourceArray.length);

        int i = 0;
        for (S source : sourceArray) {
            if (source != null) {
                destinationArray[i] = mapper.map(sourceArray[i], destinationClass);
                i++;
            }
        }

        return destinationArray;
    }


    public static <S, D> D[] mapArray(final S[] sourceArray, final Class<D> destinationClass, CBeanAfterHandler<S, D> beanAfterHandler) {
        D[] destinationArray = ArrayUtil.newArray(destinationClass, sourceArray.length);

        int i = 0;
        D d = null;
        for (S source : sourceArray) {
            if (source != null) {
                d = mapper.map(sourceArray[i], destinationClass);
                beanAfterHandler.handle(source, d);
                destinationArray[i++] = d;
            }
        }
        return destinationArray;
    }
}
