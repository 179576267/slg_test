package com.douqu.game.core.entity.ext.data.world;

import com.alibaba.druid.util.StringUtils;
import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.config.challenge.OfficialRankConfig;
import com.douqu.game.core.entity.ext.data.BaseData;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.util.LoadUtils;
import com.douqu.game.core.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author wangzhenfei
 *         2017-10-19 14:16
 *         世界排行  包括竞技场和官阶战
 */
public class WorldOfficialRankData extends BaseData {


    /**camp  rankId  data***/
    private Map<Integer, Map<Integer,List<WorldOfficialRankBean>>> officialRanksMap = new ConcurrentHashMap<>();




    @Override
    public void init() {
        List<OfficialRankConfig> list = DataFactory.getInstance().getDataList(DataFactory.OFFICIAL_RANK_KEY);
        List<WorldOfficialRankBean>  dataList;
        Map<Integer,List<WorldOfficialRankBean>> rankMap;
        List<String> nameDefault = null;
        for(int i = 0; i < list.size(); i++){
            rankMap = officialRanksMap.get(list.get(i).camp);
            if(rankMap == null){
                rankMap = new ConcurrentHashMap<>();
                officialRanksMap.put(list.get(i).camp, rankMap);
            }
            int index = list.get(i).id % 100;
            dataList = new CopyOnWriteArrayList<>();
            if(index < 6){
                if(index == 1){
                    nameDefault = LoadUtils.getRandomPlayerNameList(5);
                }
                WorldOfficialRankBean bean = new WorldOfficialRankBean();
                bean.setName(nameDefault.get(index - 1));
                dataList.add(bean);
            }
//            for(int j = 0; j < list.get(i).getMaxPerson(); j++){
//                //每个官阶初始化
//                dataList.add(new WorldOfficialRankBean());
//            }
            rankMap.put(index, dataList);

        }
    }


    @Override
    public void checkInit() {

    }

    @Override
    public void reset()
    {

    }

    @Override
    public void checkReset() {

    }


    @Override
    public void writeTo(ByteBuffer buffer)
    {
        buffer.writeByte(officialRanksMap.size());
        for (Integer camp : officialRanksMap.keySet()) {
            buffer.writeByte(camp);
            Map<Integer, List<WorldOfficialRankBean>> integerListMap = officialRanksMap.get(camp);
            buffer.writeByte(integerListMap.size());
            for (Integer id : integerListMap.keySet()) {
                buffer.writeShort(id);
                List<WorldOfficialRankBean> worldOfficialRankDatas = integerListMap.get(id);
                buffer.writeShort(worldOfficialRankDatas.size());
                for(WorldOfficialRankBean data : worldOfficialRankDatas){
                    data.writeTo(buffer);
                }
            }
            
        }
    }

    @Override
    public void loadFrom(ByteBuffer buffer)
    {
        int campSize = buffer.readByte();
        for(int i = 0; i < campSize; i++){
            int campKey = buffer.readByte();
            Map<Integer,List<WorldOfficialRankBean>> campMap = new ConcurrentHashMap<>();
            int idSize = buffer.readByte();
            for(int j = 0; j < idSize; j++){
                int idKey = buffer.readShort();
                List<WorldOfficialRankBean> datas = new CopyOnWriteArrayList<>();
                int listSize = buffer.readShort();
                for(int k = 0; k < listSize; k++){
                    WorldOfficialRankBean data = new WorldOfficialRankBean();
                    data.loadFrom(buffer);
                    data.setRankId(idKey);
                    datas.add(data);
                }
                campMap.put(idKey, datas);
            }
            officialRanksMap.put(campKey, campMap);
        }

    }

    /**
     * 根据自己的阵营和
     * @param player
     * @return
     */
    public WorldOfficialRankBean getRankInfoByObjectIndex(Player player){
        int camp = player.camp;
        String objectIndex = player.getObjectIndex();
        WorldOfficialRankBean bean = null;
        Map<Integer,List<WorldOfficialRankBean>> idsMap = officialRanksMap.get(camp);
        if(idsMap != null){
            for (Integer id : idsMap.keySet()){
                List<WorldOfficialRankBean> rankList = idsMap.get(id);
                int position = rankList.indexOf(new WorldOfficialRankBean(objectIndex));
                if(position < 0){
                    continue;
                }else {
                    bean = rankList.get(position);
                    bean.setRankId(id);
                    bean.setPosition(position);
                }

            }
        }
        return bean;

    }

    /**
     * 获取目标官阶指定位置的排行对象
     * @param player
     * @param officialRankId
     * @param position
     * @return
     */
    public WorldOfficialRankBean getTargetPositionPlayer(Player player, int officialRankId, int position){
        int camp = player.camp;
        WorldOfficialRankBean bean = null;
        try {
            bean = officialRanksMap.get(camp).get(officialRankId).get(position);
        } catch (Exception e) {
           System.out.println("获取目标官阶指定位置的排行对象不存在" );
        }

        return bean;

    }

    /**
     * 获取前五位的玩家或者npc
     * @param player
     * @return
     */
    public List<WorldOfficialRankBean> getTop5Player(Player player){
        int camp = player.camp;
        List<WorldOfficialRankBean> resultList = null;
        WorldOfficialRankBean rankBean;
        Map<Integer,List<WorldOfficialRankBean>> idsMap = officialRanksMap.get(camp);
        if(idsMap  != null){
            resultList = new ArrayList<>();
            for(int i = 1 ; i < 6; i++){
                rankBean = new WorldOfficialRankBean();
                rankBean.setRankId(i);
                List<WorldOfficialRankBean> list = idsMap.get(i);
                if(list.size() != 0){
                    rankBean.setObjectIndex(list.get(0).getObjectIndex());
                    rankBean.setName(list.get(0).getName());
                }
                resultList.add(rankBean);
            }
        }

        return resultList;

    }

    /**
     * 根据官阶id获取此官阶最多四个人信息
     * @param targetRankId
     * @return
     */
    public List<WorldOfficialRankBean> getTargetRankPlayer(Player player, int targetRankId){
        List<WorldOfficialRankBean> resultList = null;
        int camp = player.camp;
        Map<Integer,List<WorldOfficialRankBean>> idsMap = officialRanksMap.get(camp);
        if(idsMap  != null){
            List<WorldOfficialRankBean> rankList = idsMap.get(targetRankId);
            if(rankList != null){
                resultList = new ArrayList<>();
                //获取对应官阶最大人数
                OfficialRankConfig config = DataFactory.getInstance().getGameObject(DataFactory.OFFICIAL_RANK_KEY, targetRankId + 100 * camp);
                //自己所在官阶
                WorldOfficialRankBean myRank = getRankInfoByObjectIndex(player);
                int offset = 0;
                if(myRank != null && myRank.getRankId() == targetRankId){ //自己的所在的官阶就是目标官阶
                    offset = 1;
                    resultList.add(myRank);
                }
                int maxReturnNum = Math.min(config.maxPerson - offset, 4 - offset);//最大返回人数
                 List<Integer> indexList = Utils.getRandomInteger(maxReturnNum, config.maxPerson);
                int size = indexList == null ? 0 : indexList.size();
                for(int i = 0; i < size; i++){
                    WorldOfficialRankBean rankBean;
                    if(indexList.get(i) < rankList.size()) {
                        rankBean = rankList.get(indexList.get(i));
                        rankBean.setPosition(indexList.get(i));
                    }else {//随机的位置已经超出该官阶的已有人员，添加
                        //首先找到最老的未被占用的npc位置
                         int index = getMinEmptyNpcIndex(rankList, indexList);
                        if(index != -1 ){//
                            rankBean = rankList.get(index);
                            rankBean.setPosition(index);
                            indexList.set(i, index);
                        }else{
                            rankBean = new WorldOfficialRankBean();
                            rankBean.setPosition(rankList.size());
                            indexList.set(i, rankList.size());
                            rankList.add(rankBean);
                        }
                    }
                    rankBean.setRankId(targetRankId);
                    resultList.add(rankBean);
                }

            }
        }

        return resultList;
    }


    public int getMinEmptyNpcIndex(List<WorldOfficialRankBean> rankList, List<Integer> indexList){
        int index = -1;
        int size = rankList.size();
        WorldOfficialRankBean rankBean;
        for(int i = 0; i < size; i++){
            rankBean = rankList.get(i);
            if(StringUtils.isEmpty(rankBean.getObjectIndex()) && !indexList.contains(i)&&
                    !rankBean.isBattle()){// 有空的npc并且不在随机列表里面且不在被挑战
                index = i;
                break;
            }
        }
        return index;
    }



    @Override
    public String toString() {
        return officialRanksMap.toString();
    }
}
