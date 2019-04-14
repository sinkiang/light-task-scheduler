package com.github.ltsopensource.admin.access.face;

import com.github.ltsopensource.admin.request.NodePaginationReq;
import com.github.ltsopensource.admin.response.PaginationRsp;
import com.github.ltsopensource.core.cluster.Node;
import com.github.ltsopensource.core.cluster.NodeType;

import java.util.List;

/**
 * 节点访问对象
 * Created by zhangjianjun on 2017/5/26.
 */
public interface BackendNodeAccess {
    public void addNode(List<Node> nodes) ;
    public void clear();
    public void removeNode(List<Node> nodes);
    public Node getNodeByIdentity(String identity);
    public List<Node> getNodeByNodeType(NodeType nodeType);
    public List<Node> search(NodePaginationReq request);
    public PaginationRsp<Node> pageSelect(NodePaginationReq request);
}
