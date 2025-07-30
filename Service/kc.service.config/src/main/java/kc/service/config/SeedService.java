package kc.service.config;

import kc.framework.extension.DateExtensions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kc.dataaccess.config.ISysSequenceRepository;
import kc.framework.base.SeedEntity;
import kc.model.config.SysSequence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.List;
import java.util.Optional;

@Service
public class SeedService implements ISeedService {

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    private ISysSequenceRepository _sysSequenceRepository;

    @Override
    public String getSeedCodeByName(String seqName, int step) {
        StoredProcedureQuery spQuery = entityManager.createNamedStoredProcedureQuery("GetRegularDateVal");

        String currDate = DateExtensions.getNowDateString(DateExtensions.FMT_yMd1);
        StoredProcedureQuery storedProcedure = spQuery
                .setParameter("seqname", seqName)
                .setParameter("length", 5)
                .setParameter("currdate", currDate)
                .setParameter("step", step);

        return (String) storedProcedure.getOutputParameterValue("code");
    }

    @Override
    public SeedEntity getSeedEntityByName(String seqName, int step) {
        StoredProcedureQuery spQuery = entityManager.createNamedStoredProcedureQuery("GetRegularDateVal");

        String currDate = DateExtensions.getNowDateString(DateExtensions.FMT_yMd1);
        StoredProcedureQuery storedProcedure = spQuery
                .setParameter("seqname", seqName)
                .setParameter("length", 5)
                .setParameter("currdate", DateExtensions.getNowDateString(DateExtensions.FMT_yMd1))
                .setParameter("step", step);
        //storedProcedure.execute();

        return (SeedEntity)storedProcedure.getSingleResult();
    }
}
