package com.luv2code.ecommerce.config;

import com.luv2code.ecommerce.entity.Product;
import com.luv2code.ecommerce.entity.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Configuration
public class MyDataRestConfig  implements RepositoryRestConfigurer{



    private EntityManager entityManager ;

    @Autowired
    public MyDataRestConfig(EntityManager  theEntityManager){
        entityManager = theEntityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {

        HttpMethod[]  unSupportedActions = {HttpMethod.DELETE,HttpMethod.POST , HttpMethod.PUT};

        // Disable HttpMethods Put , Delete , Post For Product
        config.getExposureConfiguration()
                .forDomainType(Product.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(unSupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(unSupportedActions) );



        // Disable HttpMethods Put , Delete , Post For Product Category
        config.getExposureConfiguration()
                .forDomainType(ProductCategory.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(unSupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(unSupportedActions) );

        exposeIds(config);

    }

    private void exposeIds(RepositoryRestConfiguration config) {

        //expose entity ids

        // get list of all entity classes from data manager

        Set<EntityType<?>> entities =  entityManager.getMetamodel().getEntities();

        //create an array of the entity types

        List<Class>  entityClasses = new ArrayList<>();

        for(EntityType tempEntityType : entities){
            System.out.println("tempEntityType.getJavaType() : "+tempEntityType.getJavaType());
            entityClasses.add(tempEntityType.getJavaType());
        }

        // expose the entity ids for array of entity / domain types

        Class[] domainType = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainType);


    }
}
