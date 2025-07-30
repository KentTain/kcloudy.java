package kc.service.config;

import kc.framework.base.SeedEntity;

import java.util.List;

public interface ISeedService {

    String getSeedCodeByName(String seqName, int step);

    SeedEntity getSeedEntityByName(String seqName, int step);
}
