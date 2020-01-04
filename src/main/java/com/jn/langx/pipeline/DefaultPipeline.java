package com.jn.langx.pipeline;

public class DefaultPipeline<T> implements Pipeline<T> {
    private HeadHandlerContext head;
    private TailHandlerContext tail;
    private T target;

    public DefaultPipeline() {
        this(new HeadHandlerContext(), new TailHandlerContext());
    }

    public DefaultPipeline(Handler handler) {
        this(handler, handler);
    }

    public DefaultPipeline(Handler headHandler, Handler tailHandler) {
        this(new HeadHandlerContext(headHandler), new TailHandlerContext(tailHandler));
    }

    private DefaultPipeline(HeadHandlerContext head, TailHandlerContext tail) {
        this.head = head;
        this.tail = tail;
        this.head.setNext(tail);
        this.tail.setPrev(head);
    }

    @Override
    public void addFirst(Handler handler) {
        HandlerContext ctx = new HandlerContext(handler);
        HandlerContext first = head.getNext();

        first.setPrev(ctx);
        ctx.setNext(first);

        head.setNext(ctx);
        ctx.setPrev(head);
    }

    @Override
    public void addLast(Handler handler) {
        HandlerContext ctx = new HandlerContext(handler);
        HandlerContext last = tail.getPrev();

        last.setNext(ctx);
        ctx.setPrev(last);

        ctx.setNext(tail);
        tail.setPrev(ctx);
    }

    @Override
    public HeadHandlerContext getHead() {
        return head;
    }

    @Override
    public void clear() {
        HandlerContext ctx = getHead();
        HandlerContext next = null;
        while (ctx.hasNext()) {
            next = ctx.getNext();
            ctx.clear();
            ctx = next;
        }
        if (next != null) {
            next.clear();
        }
    }

    @Override
    public void handle() {
        getHead().inbound();
    }

    @Override
    public void setTarget(T target) {
        this.target = target;
    }

    @Override
    public T getTarget() {
        return target;
    }
}