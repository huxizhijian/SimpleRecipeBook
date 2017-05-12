package org.huxizhijian.simplerecipebook.greendao.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import org.huxizhijian.simplerecipebook.greendao.entity.RecipeEntity;
import org.huxizhijian.simplerecipebook.greendao.entity.SearchHistory;

import org.huxizhijian.simplerecipebook.greendao.gen.RecipeEntityDao;
import org.huxizhijian.simplerecipebook.greendao.gen.SearchHistoryDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig recipeEntityDaoConfig;
    private final DaoConfig searchHistoryDaoConfig;

    private final RecipeEntityDao recipeEntityDao;
    private final SearchHistoryDao searchHistoryDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        recipeEntityDaoConfig = daoConfigMap.get(RecipeEntityDao.class).clone();
        recipeEntityDaoConfig.initIdentityScope(type);

        searchHistoryDaoConfig = daoConfigMap.get(SearchHistoryDao.class).clone();
        searchHistoryDaoConfig.initIdentityScope(type);

        recipeEntityDao = new RecipeEntityDao(recipeEntityDaoConfig, this);
        searchHistoryDao = new SearchHistoryDao(searchHistoryDaoConfig, this);

        registerDao(RecipeEntity.class, recipeEntityDao);
        registerDao(SearchHistory.class, searchHistoryDao);
    }
    
    public void clear() {
        recipeEntityDaoConfig.clearIdentityScope();
        searchHistoryDaoConfig.clearIdentityScope();
    }

    public RecipeEntityDao getRecipeEntityDao() {
        return recipeEntityDao;
    }

    public SearchHistoryDao getSearchHistoryDao() {
        return searchHistoryDao;
    }

}
