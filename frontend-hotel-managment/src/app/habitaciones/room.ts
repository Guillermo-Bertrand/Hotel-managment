import { BedType } from "./bedtype";

export class Room{
    idRoom: number;
    price: number;
    roomType: string;
    description: string;
    bedType: BedType = new BedType();
}