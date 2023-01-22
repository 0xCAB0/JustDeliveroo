
# JustDeliveroo

<table>  
<tr>  
<td>  

## Food delivery mock app using Firebase for authentication,  notifications and database

- RoomDB for local DB instance
- Retrofit for HTTP requests
- MVVM architecture

</td>  
<td>

![Logo](https://github.com/alvarocabo/JustDeliveroo/blob/master/app/src/main/res/drawable/logo_mini.png?raw=true)

</td>
</tr>
</table>

## Authors

- [@alvarocabo](https://www.github.com/alvarocabo)

## Database Arch

We are using the Firebase Realtime database, which is a based on a NoSQL arch, so here are our 2 models in use for this project at the time of this realease. (v 0.1) 

![DB_Diagram](https://github.com/alvarocabo/JustDeliveroo/blob/master/docs/DB_diagram.png)

## API Reference

Here are all the request our database allows

### Get all items

```http
  GET /Comida
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `api_key` | `string` | Your API key |

Returns all the elements in the collection

## Features

- Light/dark mode toggle
- Live previews
- Fullscreen mode
- Cross platform
