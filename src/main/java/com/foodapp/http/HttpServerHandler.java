package com.foodapp.http;

import com.foodapp.commons.Log;
import com.foodapp.customer.CustomerAPI;
import com.foodapp.customer.Menu;
import com.foodapp.rastaurants.Restaurant;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpServerHandler extends ChannelInboundHandlerAdapter {
    Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

    private static final byte[] CONTENT = {'M', 'e', 'S', 'H', 'A', 'C', 'K', 'S', 'E', 'N', 'D'};

    private static final AsciiString CONTENT_TYPE = new AsciiString("Content-Type");
    private static final AsciiString CONTENT_LENGTH = new AsciiString("Content-Length");
    private static final AsciiString CONNECTION = new AsciiString("Connection");
    private static final AsciiString KEEP_ALIVE = new AsciiString("keep-alive");
    private final StringBuilder buf = new StringBuilder();
    private HttpRequest request;

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof HttpRequest) {
            HttpRequest req = this.request = (HttpRequest) msg;


            if (HttpUtil.is100ContinueExpected(req)) {
                ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
            }
            boolean keepAlive = HttpUtil.isKeepAlive(req);
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(CONTENT));
            response.headers().set(CONTENT_TYPE, "text/plain");
            response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());

            if (!keepAlive) {
                // ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                response.headers().set(CONNECTION, KEEP_ALIVE);
                // ctx.write(response);
            }
        }

        if (msg instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) msg;

            ByteBuf content = httpContent.content();
            if (content.isReadable()) {
                //  buf.append("CONTENT: ");
                buf.append(content.toString(CharsetUtil.UTF_8));
                //buf.append("\r\n");
                appendDecoderResult(buf, request);
            }

            if (msg instanceof LastHttpContent) {
                //buf.append("END OF CONTENT\r\n");

                LastHttpContent trailer = (LastHttpContent) msg;
                if (!trailer.trailingHeaders().isEmpty()) {
                    //buf.append("\r\n");
                    for (CharSequence name : trailer.trailingHeaders().names()) {
                        for (CharSequence value : trailer.trailingHeaders().getAll(name)) {
                            buf.append("TRAILING HEADER: ");
                            buf.append(name).append(" = ").append(value).append("\r\n");
                        }
                    }
                    // buf.append("\r\n");
                }

                if (!writeResponse(trailer, ctx)) {
                    // If keep-alive is off, close the connection once the content is fully written.
                    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
                }
            }
        }

    }

    private boolean writeResponse(HttpObject currentObj, ChannelHandlerContext ctx) {
        // Decide whether to close the connection or not.
        //  boolean keepAlive = HttpUtil.isKeepAlive(request);
        boolean keepAlive = false;

        String responseMessageText =ProcessRequest(request.uri(), buf.toString());
        //your response message goes here
        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, currentObj.decoderResult().isSuccess() ? OK : BAD_REQUEST,
                Unpooled.copiedBuffer(responseMessageText, CharsetUtil.UTF_8));


        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

        if (keepAlive) {
            // Add 'Content-Length' header only for a keep-alive connection.
            response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
            // Add keep alive header as per:
            // - http://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
            response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }


        // Write the response.
        ctx.write(response);

        return keepAlive;
    }

    private static void appendDecoderResult(StringBuilder buf, HttpObject o) {
        DecoderResult result = o.decoderResult();
        if (result.isSuccess()) {
            return;
        }

        buf.append(".. WITH DECODER FAILURE: ");
        buf.append(result.cause());
        buf.append("\r\n");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }


    public String ProcessRequest(String theUri, String requestMessage) {
        String resultS = "Invalid Request";
        String[] uriSplit = request.uri().split("\\?")[0].split("/");
        String endpointName = uriSplit.length > 0 ? uriSplit[uriSplit.length - 1] : "";

        System.out.println(">>> Endpoint " + endpointName + " << URI " + theUri);


        Log.w(">>> Endpoint Message " + requestMessage);

        /** customer login***/
         if (endpointName.equalsIgnoreCase("customer_login")){
            resultS = CustomerAPI.LoginHandler(requestMessage);
        }
        /*** end point for new customer **/
        else if(endpointName.equalsIgnoreCase("customer_register")){
            resultS = CustomerAPI.createNewCustomer(requestMessage);
        }
         /*** end point for new restaurant **/
         else if(endpointName.equalsIgnoreCase("restaurant_login")){
             resultS = Restaurant.RestaurantLoginHandler(requestMessage);
         }
         /*** end point for new restaurants **/
         else if(endpointName.equalsIgnoreCase("restaurant_register")){
             resultS = Restaurant.NewRestaurant(requestMessage);
         }

         /*** end point for new category **/
         else if(endpointName.equalsIgnoreCase("appCreateCategory")){
             resultS = Menu.CreateNewCategory(requestMessage);
         }
         /*** end point for new menu getAllMenu **/
         else if(endpointName.equalsIgnoreCase("appCreateMenu")){
             resultS = Menu.CreateNewMenu(requestMessage);
         }
         //get all menu details for restaurants
         else if(endpointName.equalsIgnoreCase("getAllMenu")){
             resultS = String.valueOf(Menu.getAllMenu());
         }

         else if(endpointName.equalsIgnoreCase("app_data_collect")){
            resultS = TLSSecurity.TLSHandshake(requestMessage);
        }

        return resultS;
    }


}
